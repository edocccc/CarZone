package com.polimi.carzone.persistence.serviceimpl;

import com.polimi.carzone.persistence.repository.AppuntamentoRepository;
import com.polimi.carzone.persistence.service.AppuntamentoService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class AppuntamentoServiceImpl implements AppuntamentoService {

    @Autowired
    AppuntamentoRepository appuntamentoRepo;

}
