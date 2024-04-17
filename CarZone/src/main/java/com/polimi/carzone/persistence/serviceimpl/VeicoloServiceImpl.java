package com.polimi.carzone.persistence.serviceimpl;

import com.polimi.carzone.dto.request.AggiuntaVeicoloRequestDTO;
import com.polimi.carzone.dto.request.DettagliVeicoloRequestDTO;
import com.polimi.carzone.dto.response.DettagliVeicoloResponseDTO;
import com.polimi.carzone.dto.response.VeicoloResponseDTO;
import com.polimi.carzone.model.Alimentazione;
import com.polimi.carzone.model.Veicolo;
import com.polimi.carzone.persistence.repository.VeicoloRepository;
import com.polimi.carzone.persistence.service.VeicoloService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class VeicoloServiceImpl implements VeicoloService {

    private final VeicoloRepository veicoloRepo;

    @Override
    public boolean aggiungiVeicolo(AggiuntaVeicoloRequestDTO request) {
        if(request == null ||
                request.getTarga().isEmpty() ||
                request.getTarga().isBlank() ||
                request.getMarca().isEmpty() ||
                request.getMarca().isBlank() ||
                request.getModello().isEmpty() ||
                request.getModello().isBlank() ||
                request.getChilometraggio() < 0 ||
                request.getAnnoProduzione() < 1900 ||
                request.getAnnoProduzione() > LocalDateTime.now().getYear() ||
                request.getPotenzaCv() < 0 ||
                request.getAlimentazione().isEmpty() ||
                request.getAlimentazione().isBlank() ||
                request.getPrezzo() < 0.0) {
            return false;
        }

        Veicolo veicolo = new Veicolo();
        veicolo.setTarga(request.getTarga());
        veicolo.setMarca(request.getMarca());
        veicolo.setModello(request.getModello());
        veicolo.setChilometraggio(request.getChilometraggio());
        veicolo.setAnnoProduzione(request.getAnnoProduzione());
        veicolo.setPotenzaCv(request.getPotenzaCv());

        final Alimentazione alimentazione = switch (request.getAlimentazione()) {
            case "BENZINA" -> Alimentazione.BENZINA;
            case "DIESEL" -> Alimentazione.DIESEL;
            case "IBRIDA" -> Alimentazione.IBRIDA;
            case "GPL" -> Alimentazione.GPL;
            case "ELETTRICA" -> Alimentazione.ELETTRICA;
            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Alimentazione non valida");
        };

        veicolo.setAlimentazione(alimentazione);
        veicolo.setPrezzo(request.getPrezzo());

        try{
            veicoloRepo.save(veicolo);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public List<VeicoloResponseDTO> findAll() {
        List<Veicolo> veicoli = veicoloRepo.findAll();
        List<VeicoloResponseDTO> veicoliResponse = new ArrayList<>();
        for (Veicolo veicolo : veicoli) {
            veicoliResponse.add(new VeicoloResponseDTO(
                    veicolo.getMarca(),
                    veicolo.getModello(),
                    veicolo.getPrezzo()
            ));
        }
        if(veicoliResponse.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Non ci sono veicoli disponibili");
        }
        return veicoliResponse;
    }

    @Override
    public DettagliVeicoloResponseDTO recuperaDettagli(DettagliVeicoloRequestDTO request) {
        if (request == null || request.getIdVeicolo() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id veicolo non valido");
        }
        Veicolo veicolo = veicoloRepo.findById(request.getIdVeicolo()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Veicolo non trovato"));
        DettagliVeicoloResponseDTO response = new DettagliVeicoloResponseDTO();
        response.setTarga(veicolo.getTarga());
        response.setMarca(veicolo.getMarca());
        response.setModello(veicolo.getModello());
        response.setChilometraggio(veicolo.getChilometraggio());
        response.setAnnoProduzione(veicolo.getAnnoProduzione());
        response.setPotenzaCv(veicolo.getPotenzaCv());
        response.setAlimentazione(veicolo.getAlimentazione());
        response.setPrezzo(veicolo.getPrezzo());
        return response;
    }
}
