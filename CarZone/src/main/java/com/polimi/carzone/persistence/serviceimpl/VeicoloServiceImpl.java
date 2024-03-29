package com.polimi.carzone.persistence.serviceimpl;

import com.polimi.carzone.persistence.repository.VeicoloRepository;
import com.polimi.carzone.persistence.service.VeicoloService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class VeicoloServiceImpl implements VeicoloService {

    @Autowired
    VeicoloRepository veicoloRepo;
}
