# but du jeu

Une application permettant à des utilisateurs de collectionner des citations (quotes).

Les quotes sont générées à partir de quotes15.p.rapidapi.com par le déclenchement d'utilisateurs ADMIN.

- [x] Les users s'authentifient en Basic Headers
- [x] Une citation a un identifiant, un contenu et un originator.
- [x] Elles devront être stockées dans une base de donnée, une fois générées.

Une fonctionnalité importante sera celle d'échange de citations entre utilisateurs

Un utilisateur possédant X cartes différentes pourra gagner un avantage ?
## API

- [x] GET /quotes : renvoie toutes les quotes de l'utilisateur requester
- [x] POST /quotes/generate {n: int} : génère n nouvelles citations en base de donnée
- POST /quotes/trade/offer { idQuote: int } : crée un trade pour la quote idQuote
- POST /quotes/trade/{id}/accept {idQuote: int} : accepte un trade en échangeant idQuote contre celle du trade