package com.ipiecoles.communes.web.controller;
import com.ipiecoles.communes.web.model.Commune;
import com.ipiecoles.communes.web.repository.CommuneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

}