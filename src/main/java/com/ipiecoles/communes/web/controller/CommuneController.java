package com.ipiecoles.communes.web.controller;
import com.ipiecoles.communes.web.model.Commune;
import com.ipiecoles.communes.web.repository.CommuneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;



@Controller
public class CommuneController {

    @Autowired
    private CommuneRepository communeRepository;

    @GetMapping("/communes/{codeInsee}")
    public String getCommune(
            @PathVariable String codeInsee,
            final ModelMap model)
    {
        Optional<Commune> commune = communeRepository.findById(codeInsee);
        model.put("commune", commune.get());
        return "detail";
    }

    @PostMapping(value ="/communes", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String saveNewCommune(
            Commune commune,
            final ModelMap model)
    {
        // Ajouter un certain nombre de contrôles...
        commune = communeRepository.save(commune);
        model.put("commune", commune);
        return "detail";
    }

    @PostMapping(value ="/communes/{codeInsee}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String saveExistingCommune(
            Commune commune,
            @PathVariable String codeInsee,
            final ModelMap model)
    {
        // Ajouter un certain nombre de contrôles...
        commune = communeRepository.save(commune);
        model.put("commune", commune);
        return "detail";
    }

    @GetMapping("/communes/new")
    public String newCommune(
            final ModelMap model)
    {
        model.put("commune",new Commune());
        return "detail";
    }

}