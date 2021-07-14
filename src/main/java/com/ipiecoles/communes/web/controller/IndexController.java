package com.ipiecoles.communes.web.controller;

import com.ipiecoles.communes.web.model.Commune;
import com.ipiecoles.communes.web.repository.CommuneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.Scanner;

@Controller
public class IndexController {


    @Autowired
    private CommuneRepository communeRepository;


    @GetMapping(value = "/")
    public String index(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "codeInsee") String sortProperty,
            @RequestParam(defaultValue = "ASC") String sortDirection,
            @RequestParam(required = false, defaultValue = "") String search,
            Commune commune,
            final ModelMap model) {

        //Constituer un PageRequest
        PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.fromString(sortDirection), sortProperty);
        Page<Commune> communes;


        Scanner sc = new Scanner(search);

        if(search == null || search.isEmpty()){
            //Appeler findAll si search est null
            communes = communeRepository.findAll(pageRequest);

        } else if (sc.hasNextInt()){
            // Si le search est un code insee (int) on affiche le détail de la commune
            return "redirect:/communes/" + search;
        }
        else {

            //Appeler findByNomContainingIgnoreCase si search n'est pas null
            communes = communeRepository.findByNomContainingIgnoreCase(search, pageRequest);
        }


        model.put("communes", communes);
        model.put("nbCommunes", communes.getTotalElements());
        model.put("pageSizes", Arrays.asList(5, 10, 20, 50, 100)); // ${chaine1 == chaine2} => problème
        model.put("size", size);
        model.put("search", search);
        model.put("sortDirection", sortDirection);
        model.put("sortProperty", sortProperty);


        model.put("start", (((size * page) + size) - (size - 1)));
        model.put("end", (size * page) + size);
        model.put("page", page);


        return "list"; //Chemin du template (sans .html) à partir du dossier templates
    }

}