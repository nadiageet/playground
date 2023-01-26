# But du jeu

Une application permettant à des utilisateurs de collectionner des citations (quotes).

Les quotes sont générées à partir de quotes15.p.rapidapi.com par le déclenchement d'utilisateurs ADMIN.

- [x] Les users s'authentifient sur un POST /login puis obtiennent un token JWT permettant de s'authentifier
- [x] Une citation a un identifiant, un contenu et un originator.
- [x] Elles devront être stockées dans une base de donnée, une fois générées.
Une fonctionnalité importante sera celle d'échange de citations entre utilisateurs

Un utilisateur possédant X cartes différentes pourra gagner un avantage ?
## API
API à redéfinir en fonction des objectifs ci dessous

### Objectifs principaux 
- Pour les users normaux :
  - [x] Une page montrant toutes les citations possédées et les autres en grisées afin de voir combien il en manque.
  - [x] Une page permettant de gagner une citation aléatoire parmis celle existantes.
  - [x] Une page permettant de proposer des citations à l'échange
  - [x] Une page permettant de voir les citations échangeables par les autres
  - permettre de demander un échange
  - permettre d'accepter ou refuser un échange
  - Une page récapitulant les échanges effectués
- Pour les admins :
  - [x] Une page permettant de générer aléatoirement une quote depuis le site
  - [x] Une page permettant de créer manuellement une quote, le créateur sera le nom du user
  - Une page pour voir les échanges en cours
  - Une page pour voir les collections des users
