# Vežbe 9

## WebSockets

WebSockets API je tehnologija koja omogućava otvaranje dvosmerne komunikacije između klijenta i servera. Pomoću ovog API-ja mogu da se šalju poruke serveru, ali i server može da inicira komunikaciju i šalje poruku klijentu.  

Primer rada sa [Web Socket](https://blog.bitsrc.io/deep-dive-into-websockets-e6c4c7622423)-ima i [STOMP](http://jmesnil.net/stomp-websocket/doc/)-om u Spring-u nalazi se u projektu _websockets-example_.

U primeru su prikazani:

* konfiguracija **Message Broker**-a (`configureMessageBroker(MessageBrokerRegistry registry)` metoda u klasi _WebSocketConfiguration.java_),
* registrovanje **endpoint**-a (pozivom `registry.addEndpoint("/socket")` u `registerStompEndpoints(StompEndpointRegistry registry)` metoda u klasi _WebSocketConfiguration.java_),
* stranica koja je pretplaćena na prihvatanje poruka sa jedanog **topic**-a (poziv `stompClient.subscribe(topic, callback)` metode na klijentskoj strani. _Callback_ se izvršava kada se pošalju podaci na pretplaćeni _topic_),
* slanje direktnih poruka (upotrebom [SockJS](https://github.com/sockjs/sockjs-client) klijenta) putem **REST** poziva (`public ResponseEntity<?> sendMessage(@RequestBody Map<String, String> message)` metoda u klasi _WebSocketController.java_)
* slanje poruke sa strane klijenta preko **Web Socket**-a (`public Map<String, String> broadcastNotification(String message)` metoda u klasi _WebSocketController.java_).

Podrška za korišćenje WebSockets u Spring aplikaciji se može uključiti dodavanjem odgovarajuće zavisnosti u `pom.xml`:

```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-websocket</artifactId>
</dependency>
```

### Demo aplikacije

1. Otvoriti 3 prozora u Browser-u

2. Popuniti `Your Id` polje na sva tri prozora (uneti različite vrednosti) i kliknuti na `open socket to receive custom messages` dugme na svakom prozoru. **Web Socket** komunikacija je inicijalizovana i svaki _user_ je pretplaćen na _topic_-e `/socket-publisher` i `/socket-publisher/user-id`, gde je `user-id` vrednost koju je uneo
![Slika 1](https://i.imgur.com/14JALei.png "Slika 1")

3. U `Message` polje uneti proizvoljnu poruku. Polje `User id to send the message to` ostaviti prazno. U kodu aplikacije je definisano da ukoliko se ne unese ID User-a kojem se šalje poruka, ta poruka se šalje svima koji su _subscribe_-ovani na `/socket-publisher` _topic_
![Slika 2](https://i.imgur.com/fVsoXxi.png "Slika 2")

4. Kliknuti na dugme `Send using socket subscription` ukoliko želite da poruku pošaljete na server preko **Web Socket**-a, a ukoliko želite da klijent pogodi **REST** _endpoint_, kliknite da dugme `Send using a rest controller`

5. Kada ste kliknuli na dugme `Send ...` pogodili ste neki od _endpoint_-a na serverskoj strani koji su definisani u  `WebSocketController.java` klasi. Ovi _endpoint_-i prime zahtev od klijenta i _publish_-uju poruku na odgovarajući _endpoint_. U ovom slučaju, pošto nije definisan `User ID` kojem se poruka šalje, server će poslati poruku na _topic_ `/socket-publisher`. Bez _reload_-a aplikacije, u listi **Recived messages** na klijentskoj strani, prikazuje se poruka koja je poslata u koraku 4, svim _user_-ima
![Slika 3](https://i.imgur.com/a1tswDV.png "Slika 3")

6. U `Message` polje uneti proizvoljnu poruku. U polje `User id to send the message to` uneti `User ID` kojem se poruka šalje. U kodu aplikacije je definisano da ukoliko se unese `User ID`, ta poruka se šalje samo tom _user_-u, odnosno na _topic_  `/socket-publisher/user-id`
![Slika 4](https://i.imgur.com/VEhto18.png "Slika 4")

7. Kliknuti na dugme `Send using socket subscription` ukoliko želite da poruku pošaljete na server preko **Web Socket**-a, a ukoliko želite da klijent pogodi **REST** _endpoint_, kliknite da dugme `Send using a rest controller`

8. Sada je definisan `User ID` kojem se poruka šalje, i server će poslati poruku na _topic_ `/socket-publisher/user-id`. Bez _reload_-a aplikacije, u listi **Recived messages** na klijentskoj strani, prikazuje se poruka koja je poslata u koraku 7, ali samo kod onog _user_-a koji je poruku poslao i kod onog kojem je poruka poslata.
![Slika 5](https://i.imgur.com/tqEIDOF.png "Slika 5")

### Pokretanje Spring aplikacije (Eclipse)

* importovati projekat u workspace: Import -> Maven -> Existing Maven Project
* instalirati sve dependency-je iz pom.xml
* desni klik na projekat -> Run as -> Java Application / Spring Boot app (ako je instaliran STS plugin sa Eclipse marketplace)

### Pokretanje Angular aplikacije

* [Prerequisites](https://angular.io/guide/setup-local#prerequisites)
* preko komandne linije pozicionirati se u folder _socket-angular_ i kucati naredbu: _npm install_, zatim _ng serve_

**Napomena:** Tehnologije na klijentskoj strani ne utiču na razumevanje primera. Iako je na klijentskoj strani Angular, vrlo lako možete prilagoditi primer tako što ćete **SockJS** biblioteku uključiti u Vaš projekat na način koji odgovara tehnologijama koje ste odabrali.

