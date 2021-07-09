package com.ipiecoles.communes.web.controller;
import com.ipiecoles.communes.web.model.Commune;
import com.ipiecoles.communes.web.repository.CommuneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Controller
public class CommuneController {

    public static final Double DEGRE_LAT_KM = 111d;
    public static final Double DEGRE_LONG_KM = 77d;

    @Autowired
    private CommuneRepository communeRepository;

    @GetMapping("/communes/{codeInsee}")
    public String getCommune(
            @PathVariable String codeInsee,
            @RequestParam(defaultValue = "10") Integer perimetre,
            final ModelMap model)
    {
        Optional<Commune> commune = communeRepository.findById(codeInsee);
        if(commune.isEmpty()){
            //Gère une exception
            throw new EntityNotFoundException("Impossible de trouver la commune de code INSEE " + codeInsee);
            //model.put("message", "Impossible de trouver la commune de code INSEE " + codeInsee);
            //return "error";//template error qui affiche un message d'erreur
        }

        //Récupérer les communes proches de celle-ci
        model.put("commune", commune.get());
        model.put("perimetre", perimetre);
        model.put("communesProches", this.findCommunesProches(commune.get(), perimetre));
        model.put("newCommune", false);

        return "detail";
    }

    @PostMapping(value ="/communes", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String saveNewCommune(
            @Valid Commune commune,
            //Juste après le paramètre marqué @Valid
            final BindingResult result,
            RedirectAttributes attributes,
            final ModelMap model)
    {
        //S'il n'y a pas d'erreurs de validation sur le paramètre commune
        if (!result.hasErrors()){
            commune = communeRepository.save(commune);
            model.put("commune", commune);
            attributes.addFlashAttribute("type", "success");
            attributes.addFlashAttribute("message", "Enregistrement de la nouvelle commune effectué avec succès.");
            return "redirect:/communes/" + commune.getCodeInsee();
        }
        //S'il y a des erreurs...
        //Possibilité 1 : Rediriger l'utilisateur vers la page générique d'erreur
        //Possibilité 2 : Laisse sur la même page en affichant les erreurs pour chaque champ
        model.addAttribute("type", "danger");
        model.addAttribute("message", "Erreur lors de la sauvegarde de la commune");
        model.put("newCommune", true);
        return "detail";
    }

    @PostMapping(value = "/communes/{codeInsee}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String saveExistingCommune(
            @Valid Commune commune,
            //Juste après le paramètre marqué @Valid
            final BindingResult result,
            @PathVariable String codeInsee,
            final ModelMap model,
            RedirectAttributes attributes) {
        //S'il n'y a pas d'erreurs de validation sur le paramètre commune
        if (!result.hasErrors()){
            commune = communeRepository.save(commune);
            attributes.addFlashAttribute("type", "success");
            attributes.addFlashAttribute("message", "Enregistrement de la commune effectué !");
            return "redirect:/communes/" + commune.getCodeInsee();
        }
        //S'il y a des erreurs...
        //Possibilité 1 : Rediriger l'utilisateur vers la page générique d'erreur
        //Possibilité 2 : Laisse sur la même page en affichant les erreurs pour chaque champ
        model.addAttribute("type", "danger");
        model.addAttribute("message", "Erreur lors de la sauvegarde de la commune");
        // return "error";
        return "detail";
    }

    @GetMapping("/communes/{codeInsee}/delete")
    public String deleteCommune(
            @PathVariable String codeInsee,
            RedirectAttributes attributes)
    {
        Optional<Commune> commune = communeRepository.findById(codeInsee);
        if(commune.isPresent())
        {
            communeRepository.deleteById(codeInsee);
        } else {
            throw new EntityNotFoundException("Impossible de trouver la commune ayant pour code INSEE: " + codeInsee);
        }
        attributes.addFlashAttribute("type", "success");
        attributes.addFlashAttribute("message", "Suppression de la commune effectué avec succès.");
        return "redirect:/";
    }

    @GetMapping("/communes/new")
    public String newCommune(
            final ModelMap model)
    {
        model.put("commune",new Commune());
        model.put("newCommune", true);
        return "detail";
    }


    /**
     * Récupère une liste des communes dans un périmètre autour d'une commune
     * @param commune La commune sur laquelle porte la recherche
     * @param perimetreEnKm Le périmètre de recherche en kilomètre
     * @return La liste des communes triées de la plus proche à la plus lointaine
     */
    private List<Commune> findCommunesProches(Commune commune,
                                              Integer perimetreEnKm) {


        if (perimetreEnKm > 20) {
            throw new IllegalArgumentException("Le périmètre doit être égal ou inférieur à 20 KM.");
        }

        Double latMin, latMax, longMin, longMax, degreLat, degreLong;
        //1 degré latitude = 111km, 1 degré longitude = 77km
        degreLat = perimetreEnKm/DEGRE_LAT_KM;
        degreLong = perimetreEnKm/DEGRE_LONG_KM;
        latMin = commune.getLatitude() - degreLat;
        latMax = commune.getLatitude() + degreLat;
        longMin = commune.getLongitude() - degreLong;
        longMax = commune.getLongitude() + degreLong;
        List<Commune> communesProches = communeRepository.findByLatitudeBetweenAndLongitudeBetween(latMin, latMax, longMin, longMax);
        ;

        return communesProches.stream().
                filter(commune1 -> !commune1.getNom().equals(commune.getNom()) && commune1.getDistance(commune.getLatitude(), commune.getLongitude()) <= perimetreEnKm).
                sorted(Comparator.comparing(o -> o.getDistance(commune.getLatitude(), commune.getLongitude()))).
                collect(Collectors.toList());
    }

}