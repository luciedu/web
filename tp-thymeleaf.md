# Ecran liste 

- [x] Le bouton "Nouvelle commune" dirige l'utilisateur vers l'écran de création de commune
- [ ] Le tableau présente les résultats paginés de toutes les communes ou celles correspondant à la recherche effectuée
- [ ] Il est possible de trier par code insee, code postal, nom, latitude et longitude, de manière ascendante, descendante. Ajuster l'affichage de chacune des colonnes en fonction des critères de tri
- [ ] Chaque ligne commune possède un lien Détail permettant de consulter les détails d'une commune
- [ ] Il est possible de sélectionner parmi une liste (5, 10, 20, 50, 100) la taille des pages. Lorsque l'on sélectionne une taille, la page est rechargée. Il est important que la valeur sélectionnée dans la liste déroulante soit celle spécifiée dans l'url (attribut selected=true dans l'option)
- [ ] Même travail avec la sélection de la page (utiliser #numbers.sequence pour générer la liste côté Thymeleaf)
- [ ] Les boutons précédent et suivant doivent être opérationnels et conserver les critères de tri et de recherche. Précédent est désactivé en première page. Suivant est désactivé en dernière page.
- [ ] On affiche la position des résultats "Affichage des communes X à Y sur un total de Z". Attention au total qui est soit égal au nombre total de communes un base, ou égal ou nombre de résultats de la recherche

# Ecran détail
- [ ] Le titre est soit "Détail de la communes CODEINSEE, NOM" si c'est une modification ou "Création d'une nouvelle commune" si c'est une création
- [ ] On affiche dans les champs du formulaires les valeurs de la commune éditée (vide si création)
- [ ] On affiche la carte avec un marqueur à la latitude/longitude de la commune. Il faut spécifier également la "boite" (LATITUDE et LONGITUDE à remplacer avec les valeurs de la commune) : https://www.openstreetmap.org/export/embed.html?bbox=${LONGITUDE-0.10}%2C${LATITUDE-0.5}%2C${LONGITUDE+0.10}%2C${LATITUDE+0.5}&amp;layer=mapnik&amp;marker={$LATITUDE}%2C${LONGITUDE}.
- [ ] La carte n'est pas visible lors d'une création
- [ ] On affiche les communes dans un périmètre de X km autour de la commune. On peut modifier le périmètre dans l'input, cliquer sur Rechercher pour afficher de nouveaux les détails de la commune avec le périmètre modifié
- [ ] La section "périmètre" n'est pas visible lors d'une création
- [ ] Le bouton Enregistrer fait un POST sur /communes dans le cas d'une création, sur /communes/CODEINSEE dans le cas d'une modification
- [ ] Le bouton Supprimer fait un GET sur /communes/CODEINSEE/delete est n'est visible qu'en mode modification.
- [ ] Faire en sorte que lors de la modification ou de la création d'une commune, on soit redirigé vers la page de détail de la communes (pour éviter le problème de F5)
- [ ] Faire en sorte que lors de la suppression d'une commune, on soit redirigé vers la liste des communes
