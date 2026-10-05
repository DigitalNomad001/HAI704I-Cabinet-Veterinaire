# Cabinet vétérinaire — Java RMI

HAI704I — Architectures logicielles distribuées · TP 1 · Partie A · 2026–2027
[LI Hao] — [numéro étudiant:21915713] — [parcours:GL]


## 0.Introduction du projet
Cette application est un petit logiciel de **gestion de patients pour un cabinet vétérinaire**.
Elle permet aux vétérinaires du cabinet d'enregistrer et de suivre leurs patients (les animaux),
chacun depuis son propre poste.

Fonctionnalités principales :

- consulter la liste des patients et rechercher un patient par son nom ;
- enregistrer un nouveau patient (nom, maître, race, espèce) ;
- consulter et mettre à jour le dossier de suivi d'un patient (état de santé, historique) ;
- recevoir une alerte lorsque le nombre de patients franchit 100, 500 ou 1000.

Les données sont centralisées sur un **serveur** unique ; plusieurs vétérinaires peuvent s'y
connecter **en même temps** grâce à un **client en ligne de commande**. Une modification faite
par un vétérinaire (par exemple sur le dossier d'un patient)  peut être consulter par
les autres.

L'application est réalisée en **Java RMI** (Remote Method Invocation), sans framework ni
dépendance externe, dans le cadre du TP 1 de l'UE HAI704I.


## 1. Environnement

| Élément | Valeur |
|---|---|
| JDK | [OpenJDK 21.0.12]  (vérifier avec `java -version`) |
| Système | [Ubuntu 24.04] |
| IDE | [IntelliJ IDEA 2025.x] — ou ligne de commande |
| Dépendances | aucune (bibliothèque standard Java uniquement) |
| Modules JPMS | aucun `module-info.java` (projets sur le classpath) |

## 2. Structure du projet

```
tp_rmi/
├── common/   contrat partagé : interfaces distantes et classes transmises par valeur
├── server/   implémentations (servants) et lanceur du serveur   — dépend de common
└── client/   client en ligne de commande                         — dépend de common
```

- `client` ne dépend **jamais** de `server`.
- [Lister brièvement les classes principales de chaque projet, ex. :]
    - common : `Animal`, `DossierSuivi`, `Cabinet`, `CabinetObserver` (distants) ;
      `Espece` (sérialisable) ; `PatientInconnuException`, `PatientExistantException`
    - server : `AnimalImpl`, `DossierSuiviImpl`, `CabinetImpl`, `ServerCabinet` (lanceur)
    - client : `ClientMain` (point d'entrée), `CabinetService` (logique client),
      `ConsoleUI` (interface console), `ObservateurImpl` (observateur des alertes)

## 3. Compilation

Depuis le dossier `tp_rmi/` :

```bash
javac -d out/common $(find common/src -name "*.java")
javac -cp out/common -d out/server $(find server/src -name "*.java")
javac -cp out/common -d out/client $(find client/src -name "*.java")
```

Sous IntelliJ : *Build → Rebuild Project* (classes produites dans `out/production/`).

## 4. Lancement

Port du registre : **1099**. Mode de registre : **[interne — créé par le serveur]**.

Ordre de démarrage, chacun dans un terminal séparé :

```bash
# 1) Serveur
java -cp out/common:out/server vete.server.ServerCabinet
# 2) Un ou plusieurs clients
java -cp out/common:out/client vete.client.ClientMain [hote] [port]
```

Arguments du client : `hote` (défaut `localhost`), `port` (défaut `1099`).
Sous Windows, remplacer `:` par `;` dans le classpath.

**[Le serveur démarre avec 98 patients par défaut pour tester les alertes.]**

## 5. Utilisation de la CLI

```
1. Lister les patients
2. Rechercher un patient (fiche + dossier)
3. Enregistrer un nouveau patient
4. Modifier l'état de santé
5. Ajouter une observation au dossier
6. S'abonner aux alertes
7. Se désabonner des alertes
0. Quitter
```

- Taper le numéro puis Entrée ; chaque opération demande ses champs un par un.
- Recherche par nom insensible à la casse.
- [Conventions de saisie : champs obligatoires, espérance de vie = nombre (ex. 12 ou 12.5)…]
- Les alertes (seuils 100, 500, 1000, à la hausse et à la baisse) s'affichent
  automatiquement pour les clients abonnés : `>>> ALERTE : ...`
- `0` : désabonnement, libération de l'observateur, fin du processus.

## 6. Cas d'erreur gérés

| Situation | Comportement |
|---|---|
| Serveur absent au lancement du client | message, arrêt du client |
| Serveur arrêté pendant l'utilisation | « Serveur injoignable », le client continue |
| Patient introuvable | message (exception métier), retour au menu |
| Nom déjà utilisé | message, retour au menu |
| Saisie invalide | message |
| Client abonné fermé brutalement | le serveur le retire et continue |

## 7. Liens

- Dépôt Git : [URL]
- Vidéo de démonstration : [URL ou chemin dans l'archive]