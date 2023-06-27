# Mechanical Appointment

## Sommario

1. ***Descrizione del progetto***
2. ***Descrizione di Springboot e Hibernate***
3. ***Analisi Funzionale*** 
    - *****************Sequence Diagram: creazione di un appuntamento con MechanicalAction → STANDARD*
    - *****************Sequence Diagram: creazione di un appuntamento con MechanicalAction → CUSTOM*
    - *****************Use Case Diagram: funzionalità per Cliente e Meccanico*
    - *Use Case Diagram: recensione del cliente dopo lavoro effettuato*
4. ***Definizione delle entità, come sono collegate e Class Diagram***
    - *Class Diagram*
    - *Package Diagram*
5. ***Design della base di dati e descrizione MySQL | H2***
    - *diagramma E/R*
6. ***Dettaglio dei pattern utilizzati***
    - *Singleton*
    - *Factory Method Pattern*
    - *JDBC Template*
    - *Builder Pattern*
    - *RestFUL API Pattern*
    - *Flyweight Pattern*
7. *********API*********
8. ************************Security************************
    - *************Start & Configurazione*************
        - *Servlet Authentication Architecture*
        - *AbstractAuthenticationProcessingFilter*
    - ************************************Flusso per refresh token Springboot con JWT************************************
    - *Flusso per il recupero della password*
9. ***************************************************************************Email per comunicazioni e Google Calendar***************************************************************************
10. ***Testing***
    - *Annotazioni per i test JUnit*
    - *Metodi della classe di Assert*
    - *Coverage Summary*
    - *Coverage Breakdown*
11. *********************************************************************************Docker e build del progetto*********************************************************************************

---

## 1. Descrizione del progetto

Si vuole realizzare una piattaforma per la gestione di interventi meccanici, nata dalla richiesta di un ingegnere meccanico specializzato in calibrazione delle performance. 

L'ingegnere si appoggia a varie officine meccaniche in Italia, dove pianifica giornate di intervento ogni mese.

Il problema delle piattaforme esistenti per la prenotazione di appuntamenti è la gestione di più interventi contemporaneamente. 

Ciò può essere risolto grazie alla creazione di una piattaforma dedicata con tempi esterni e interni calibrati per ogni intervento, che prenda in considerazione i tempi di risoluzione. 

La piattaforma userà Angular per il front-end e Java con Spring Boot per il back-end, con api REST, repository con database relazionale, gestione degli utenti e dei ruoli, invio di email, testing e deploy con container Docker per l'entrata in produzione.

Lo scopo della piattaforma è quello di gestire la prenotazione e l'esecuzione di interventi meccanici su veicoli, permettendo agli utenti di prenotare un intervento in una determinata officina e ai gestori di gestire le prenotazioni, assegnare i tempi di intervento, visualizzare gli eventi in parallelo per le singole officine e gestire gli interventi in modo efficiente. 

Essa prevede una gestione avanzata dei tempi di intervento per evitare sovrapposizioni e ottimizzare la pianificazione degli interventi. Inoltre, verrà utilizzato un sistema di notifica via email per comunicare agli utenti gli aggiornamenti sullo stato della loro prenotazione.

---

## 2. Descrizione di Springboot e Hibernate

Java Spring Framework (Spring Framework) è uno dei framework open source più diffusi a livello aziendale per la creazione di applicazioni Java. Il framework è adatto a gli ambienti di produzione che vengono eseguiti sulla Java Virtual Machine (JVM).

**Spring Boot** è uno strumento che semplifica e accelera lo sviluppo di applicazioni web e microservizi basati su Spring Framework, grazie a tre funzionalità principali:

1. Configurazione automatica: Spring Boot può configurare automaticamente le dipendenze necessarie (utilizzando Maven o Gradle ) per l'applicazione in modo da evitare la necessità di configurare manualmente ogni singolo componente.
2. Approccio categorico alla configurazione: Spring Boot rende più facile e intuitivo organizzare le impostazioni in base alle esigenze dell'applicazione, soprattutto la sezione relativa alla sicurezza.
3. Capacità di creare applicazioni autonome: le app possono essere eseguite senza la necessità di un server applicativo esterno.

Tutte queste funzionalità si combinano per fornire uno strumento potente e flessibile che semplifica notevolmente la creazione di applicazioni backend riducendo la necessità di configurazione e installazione.

### Maven

Nel progetto ho utilizzato Maven: uno strumento open source per la gestione e l'automazione della build di progetti Java. La sua funzione principale è quella centralizzare le dipendenze del progetto, aiutando in fase di compilazione.

Il progetto è organizzato in una struttura predefinita con una serie di file di configurazione, tra cui il file pom.xml (Project Object Model). Questo file contiene informazioni sul progetto, come la versione, le dipendenze, i plugin, i test e le risorse necessarie per la compilazione.

### Hibernate e JPA

Per la persistenza dei dati registrati nell’applicativo, ho scelto di utilizzare Hibernate e JPA. 

Hibernate è un ampio ecosistema di librerie, un framework open source di Object Relational Mapping (ORM). 

E’ uno strumento di sviluppo Java che consente di mappare i modelli di entita’ orientati agli oggetti su un database relazionale, consente di salvare i dati in modo permanente dal context di Java al database.

Le specifiche JPA (Java Persistence API) per la persistenza dei dati, garantiscono una maggiore portabilità delle applicazioni.

---

## 3. Analisi funzionale

La funzionalita’ core del progetto e’ la pianificazione degli appuntamenti concorrenti. Analizziamo il flusso con cui viene richiesto un nuovo appuntamento “Standard”. 

1. L’utente si registra ed associa un veicolo, inserendo tutti i dati necessari a completare il profilo;
2. Ricerca l’open day a cui vuole partecipare, filtrando tra le varie officine sparse nel territorio Italiano;
3. Seleziona il veicolo di interesse;
4. Seleziona la MechanicalAction da intraprendere:
    1. Se viene scelta una MechanicalAction custom verranno aggiunti i tempi internalTime e externalTime manualmente dal meccanico;
    2. Se sceglie una MechanicalAction predefinita viene mostrata una externalDuration
5. Riceve tutti i time-slot disponibili per l’openDay selezionato e ne seleziona uno. 

*Esempio di come ricavare i time-slot:*

**MechanicalAction** : Mappa centralina

**internalDuration** : 2h

**externalDuration** : 8h

**Calendario meccanico** : Apertura ore 6:00 e chiusura ore 16:00

| 6:00 | occupato |
| --- | --- |
| 7:00 → 8:00 |  |
| 8:00 → 9:00 |  |
| 9:00 → 10:00 |  |
| 10:00 →11:00 |  |
| 11:00 → 12:00 | occupato |
| 12:00 → 13:00  | occupato |
| 13:00 → 14:00 | occupato |
| 14:00 → 15:00 |  |
| 15:00 → 16:00 |  |

Viene fatta una ricerca a BE per trovare tutti i time slot che sono compatibili con l’internalDuration della MechanicalAction, In questo caso avremmo [7.00 - 9.00] - [8.00 - 10.00] - [9.00- 11:00]  - [14:00 - 16:00].

Tra questi bisogna verificare che il tempo di fine dell’intervento segnalato all’utente (externalDuration) rienti all’interno dell’orario lavorativo. 

Per questo motivo dobbiamo escludere le fasce orarie [9.00- 11:00]  - [14:00 - 16:00].

Quindi dal punto di vista del meccanico la MechanicalAction può essere eseguita nelle fasce [7.00 - 9.00] - [8.00 - 10.00] .

I time-slot segnalati all’utente saranno [7:00 → 15:00] e [8:00 →16:00]

1. Dopo aver selezionato il time-slot viene inviata una mail con il riepilogo della prenotazione all’utente;
2. Il meccanico visualizza la prenotazione e modifica lo stato in *CONFIRMED o REJECTED*
3. Dopo l’intervento il meccanico modifica lo stato in *FINISHED ,* e viene inviata una mail di riepilogo all’utente, chiedendo di lasciare una recensione (Allega il collegamento alla pagina)
    
    ![Untitled.svg](Mechanical%20Appointment%2039a8ed246b424c1ba0c079d2e0b08fdb/Untitled.svg)
    

### Flusso creazione di un appuntamento con MechanicalAction → CUSTOM

1. L’utente si registra ed associa un veicolo;
2. Ricerca l’open day a cui vuole partecipare;
3. Seleziona il veicolo di interesse;
4. Scrive sotto forma di commento di cosa ha bisogno. La prenotazione viene marcata con *isMechanicalActionCustom = true*;
5. Il meccanico visualizza la prenotazione, ed ha bisogno di monitorare la disponibilita’ degli orari, quindi inserisce manualmente internalTime e externalTime;
6. Viene inviata una mail con il riepilogo della prenotazione all’utente, la prenotazione e’ nello stato *AWAITING_APPROVAL*;
7. Viene fatta una chiamata con cui vengono identificati i time-slot compatibili e conferma. Modifica lo stato in *CONFIRMED o REJECTED*;
8. Viene notificato all’utente lo stato;
9. Dopo l’intervento il meccanico modifica lo stato in *FINISHED ,* e viene inviata una mail di riepilogo all’utente, chiedendo di lasciare una recensione (Allega il collegamento alla pagina)
    
    ![Untitled (2).svg](Mechanical%20Appointment%2039a8ed246b424c1ba0c079d2e0b08fdb/Untitled_(2).svg)
    

Durante tutte le fasi sopra descritte, verranno inviate delle email per notificare all’utente la modifica o la creazione di un nuovo appuntamento. Questa sezione verra’ approfondita in seguito.

L’utente potrà:

- Registrarsi ed accedere, modificare e recuperare la sua password
- Visualizzare lo storico delle prenotazioni dei vari veicoli
- Visualizzare tutte le officine ed i relativi OpenDay
- Cancellare una prenotazione
- Scrivere una recensione
- Registrare un veicolo

![Funzionalità per i 2 attori principali: cliente e meccanico](Mechanical%20Appointment%2039a8ed246b424c1ba0c079d2e0b08fdb/Untitled.png)

Funzionalità per i 2 attori principali: cliente e meccanico

![Use Case: flusso per recensione del cliente dopo un lavoro effettuato](Mechanical%20Appointment%2039a8ed246b424c1ba0c079d2e0b08fdb/Untitled%201.png)

Use Case: flusso per recensione del cliente dopo un lavoro effettuato

---

## 4. Definizione delle entità, come sono collegate e diagrammi

Le entita’ di progetto principali sono contenute all’interno del package entity : 

- **Appointment** → e’ una relazione tra il veicolo e l’OpenDay presso una specifica officina, e contiene tutti i dati di dettaglio necessari.
- **AppointmentStatus**→  e’ un enum , e contiene tutti i possibili stati di un’appuntamento:
    - *AWAITING_APPROVAL*
    - *FINISHED*
    - *CONFIRMED*
    - *REJECTED*
- **Authority** → Ogni utente ha dei ruoli predefiniti ed un chiaro filtro delle responsabilita’ ad esso associate.
- **Garage** → Contiene informazioni riguardo il luogo e le prenotazioni che ha registrato per i vari OpenDay, piu’ informazioni di dettaglio
- **MechanicalAction** → Rappresenta l’intervento che il cliente puo’ selezionare o richiedere
- **OpenDay** → E’ una giornata in cui una particolare officina potra’ ricevere degli appuntamenti . E’ caratterizzato da un’orario lavorativo, specificando inizio e fine e le pause durante la giornata (ad esempio la pausa pranzo).
- **PasswordResetToken** → Contiene i token generati durante la fase di richiesta per il recupero della password.
- **Place** → Contiene le informazioni geografiche dei comuni italiani:
    - ISTAT
    - Comune
    - Regione
    - Provincia
- **RefreshToken** → Viene utilizzato per richiedere un nuovo JWT Token di autenticazione.
- **User** → contiene i dati dell’ utente, i suoi veicoli e le sue recensioni .
- **UserPrincipal** → Contiene tutte le informazioni di contesto, necessarie a Springboot per identificare l’utente all’interno del context.
- **Vehicle** → Identificato da dati di modello e targa contiene una lista di appuntamenti ed e’ referenziato ad un’utente specifico
- **Vote** → Ogni utente puo’ fornire, a seguito di un’appuntamento, una sua valutazione, fornendo un commento ed una valutazione da 0 a 5 stelle.
- **DayPlan** → Viene utilizzato internamente da Openday per la definizione degli orari lavorativi
- **GoogleCalendarCreateEvent** → Classe di appoggio per la creazione di eventi da aggiungere al calendario di Google.
- Ogni appuntamento viene contenuto all’interno di un’oggetto di tipo **TimePeriod** per semplificarne la gestione.

### Diagramma delle classi

![class-diagram.png](Mechanical%20Appointment%2039a8ed246b424c1ba0c079d2e0b08fdb/class-diagram.png)

### Package Diagram

![Untitled](Mechanical%20Appointment%2039a8ed246b424c1ba0c079d2e0b08fdb/Untitled%202.png)

---

## 5. Design della base di dati e descrizione MySQL | H2

Nel progetto , vista la chiara struttura relazionale, e’ stato utilizzato **MySQL**, il quale è un sistema di gestione di database relazionali open source, utilizzato per la gestione dei dati in applicazioni web e software in generale, è altamente scalabile e può gestire grandi volumi di dati e molteplici connessioni contemporanee. 

**MySQL** è compatibile con un'ampia gamma di piattaforme, tra cui *Windows*, *Linux* e *macOS*.

### Diagramma E/R

![Untitled](Mechanical%20Appointment%2039a8ed246b424c1ba0c079d2e0b08fdb/Untitled%203.png)

### Analisi delle relazioni

**Appointment**: 

- OpenDay ManyToOne
- MechanicalAction ManyToOne
- Vehicle ManyToOne
- votes OneToMany

**Garage**

- Place ManyToOne
- openDay OneToMany
- votes OneToMany

**OpenDay**

- appointments OneToMany
- garage ManyToOne

**Place**

- garage OneToMany

**User**

- votes OneToMany
- vehicle OneToMany
- roles ManyToMany

**Vehicle**

- reservation OneToMany

**Vote**

- user ManyToOne
- garage ManyToOne
- appointment ManyToOne

---

## 6. Dettaglio dei pattern utilizzati

I design pattern sono una parte essenziale dello sviluppo del software. Queste soluzioni non solo risolvono i problemi ricorrenti, ma aiutano anche gli sviluppatori a comprendere il design delle strutture riconoscendo gli schemi comuni.

I pattern utilizzati che sono stati utilizzati lato Backend sono : 

### 1. Singleton

Il *Singleton* è un modello di progettazione creazionale, che garantisce l'esistenza di un solo oggetto del suo genere e fornisce un unico punto di accesso ad esso per qualsiasi altro codice.

Il service **AppointmentCore**, e’ una classe singleton , e viene richiamata dai vari service :  

```bash
public class AppointmentCore {

    //singleton pattern
    private static AppointmentCore instance = null;

    private AppointmentCore() {
    }

    public static AppointmentCore getInstance() {
        if (instance == null) {
            instance = new AppointmentCore();
        }
        return instance;
    }
 ...
}
```

Spring limita un singleton a un solo oggetto per ogni contenitore IoC. In pratica, questo significa che Spring creerà un solo bean per ogni tipo di contesto applicativo.

L'approccio di Spring differisce dalla definizione rigorosa di singleton, poiché un'applicazione può avere più di un contenitore Spring. Pertanto, in una singola applicazione possono esistere più oggetti della stessa classe, se si dispone di più contenitori.

<aside>
💡 ****Autowired Singletons****

il @****Autowired**** richiama l’istanza del servizio, creando a tutti gli effetti un singleton.

</aside>

### 2. ****Factory Method Pattern****

Il pattern del metodo factory prevede una classe factory con un metodo astratto per creare l'oggetto desiderato. Tutti i controller ed i relativi service utilizzano una interfaccia per la definizione dei comportamenti.

![Untitled](Mechanical%20Appointment%2039a8ed246b424c1ba0c079d2e0b08fdb/Untitled%204.png)

![Untitled](Mechanical%20Appointment%2039a8ed246b424c1ba0c079d2e0b08fdb/Untitled%205.png)

### 3. ****JdbcTemplate****

La classe *JdbcTemplate* è la classe centrale del pacchetto JDBC. Semplifica l'uso di JDBC e aiuta a evitare gli errori più comuni. Esegue il flusso di lavoro JDBC di base, lasciando al codice dell'applicazione il compito di fornire l'SQL e di estrarre i risultati.

Rappresenta quindi il pattern Principale per l’utilizzo di repository.

### 4. Builder Pattern

**Builder** è un modello di progettazione creazionale, che consente di costruire oggetti complessi passo dopo passo. A differenza di altri modelli di creazione, Builder non richiede che gli oggetti abbiano un'interfaccia comune. Ciò rende possibile la creazione di oggetti diversi utilizzando lo stesso processo di costruzione.

![Untitled](Mechanical%20Appointment%2039a8ed246b424c1ba0c079d2e0b08fdb/Untitled%206.png)

Tutte le classi model hanno la loro implementazione del builder: 

![Untitled](Mechanical%20Appointment%2039a8ed246b424c1ba0c079d2e0b08fdb/Untitled%207.png)

### 5. RestFul API Pattern

Lo stile architetturale REST ha vincoli ben definiti che aiutano lo sviluppatore a scrivere interfacce di servizi Web scalabili.

**Elenchi e paginazione**
Un client può recuperare più elementi con le api di */filter* e filtrarli con i parametri della query. Il risultato deve essere paginato; le paginazioni più utilizzate sono la paginazione a cursore e la paginazione a offset. Ogni paginazione ha un parametro di input che limita il numero di elementi che la pagina conterrà, fornito come parametro di query, chiamiamolo limite.

### 6. ****Flyweight**** Pattern

La sezione algoritmica per la rilevazione degli slot temporali disponibili era molto complessa e strutturata, ed a seguito di misurazioni ho notato che l’esecuzione era rallentata dal continuo riutilizzo di variabili simili, istanziate inutilmente.

**Flyweight** è un pattern di design strutturale che consente ai programmi di supportare una vasta quantità di oggetti mantenendo basso il loro consumo di memoria.

Ad esempio molti appuntamenti hanno dati duplicati. Per questo motivo, possiamo applicare il modello Flyweight e memorizzare questi valori all'interno di oggetti **HashMap** separati invece di memorizzare i dati stessi in migliaia di oggetti singoli, faremo riferimento a uno degli oggetti Flyweight con un particolare set di valori.

Si guardi per riferimento la classe **`AppointmentCore`**

---

## 7. API

Un'API è un'interfaccia attraverso la quale un programma o un sito web dialoga con un altro.
Vengono utilizzate per condividere dati e servizi e sono disponibili in molti formati e tipi diversi.
formati e tipi diversi.

REST (Representational State Transfer) descrive le regole generali per la rappresentazione dei dati e dei servizi attraverso l'API. 

Tipi di richieste:
**GET →**  Leggere i dati
**POST →**  Creare/inserire dati
**PUT →** Aggiornare i dati
**DELETE →** Eliminare i dati
Invio le richieste API di tipo GET, POST, PUT, DELETE a endpoint/metodo API e ottengo la risposta.

Nell’applicativo sono presenti 113 Api , divise per servizi e categorie:

### [Appointment](about:blank#Appointment)

- `[post /api/appointments/{appointmentId}/state/{status}](about:blank#appointmentStateUpdate)`
- `[delete /api/appointments/{id}](about:blank#delete5)`
- `[delete /api/appointments/vote/{id}](about:blank#deleteVote)`
- `[get /api/appointments](about:blank#findAll5)`
- `[get /api/appointments/appointmentsUser/{id}](about:blank#findAllByUserId)`
- `[get /api/appointments/myappointments](about:blank#findAllByUserPrincipal)`
- `[get /api/appointments/{id}](about:blank#findById5)`
- `[post /api/appointments/availableTimeSlots](about:blank#getAvailableAppointmentsTimeSlots)`
- `[post /api/appointments/customAppointment](about:blank#handleCustomAppointment)`
- `[put /api/appointments/vote/{id}](about:blank#modifyVote)`
- `[post /api/appointments](about:blank#save4)`
- `[put /api/appointments/{id}](about:blank#update4)`
- `post /api/appointments/vote`

### [AuthController](about:blank#AuthController)

- `[post /api/auth/login](about:blank#authenticateUser)`
- `[post /api/auth/creaUtenteIniziale](about:blank#creaUtenteIniziale)`
- `[get /api/auth/tokenResetPassword](about:blank#getAuthenticationToChangePassword)`
- `[get /api/auth/session](about:blank#getCredentialsOAuth2)`
- `[post /api/auth/recoveryPassword](about:blank#recoveryPassword)`
- `[post /api/auth/refreshToken](about:blank#refreshtoken)`
- `[post /api/auth/signup](about:blank#registerUser)`

### [Emails](about:blank#Emails)

- `[get /api/emails/sendCustomAppointmentApprovedMail/{appointmentId}](about:blank#sendAppointmentApprovedMail)`
- `[get /api/emails/sendCustomAppointmentRejectedMail/{appointmentId}](about:blank#sendAppointmentRejectedMail)`
- `[get /api/emails/sendDeletedAppointmentData/{appointmentId}](about:blank#sendDeletedAppointmentData)`
- `[get /api/emails/sendFinishedAppointmentData/{appointmentId}](about:blank#sendFinishedAppointmentData)`
- `[get /api/emails/sendNewAppointmentMail/{appointmentId}](about:blank#sendNewAppointmentMail)`

### [Garage](about:blank#Garage)

- `[delete /api/garages/{id}](about:blank#delete4)`
- `[get /api/garages](about:blank#findAll4)`
- `[get /api/garages/{id}](about:blank#findById4)`
- `[get /api/garages/findGarageByPlaceMunicipalityStartsWith](about:blank#findGarageByPlaceMunicipalityStartsWith)`
- `[get /api/garages/findGarageByPlaceProvinceStartsWith](about:blank#findGarageByPlaceProvinceStartsWith)`
- `[get /api/garages/findGarageByPlaceRegionStartsWith](about:blank#findGarageByPlaceRegionStartsWith)`
- `[post /api/garages](about:blank#save3)`
- `[put /api/garages/{id}](about:blank#update3)`

### [MechanicalAction](about:blank#MechanicalAction)

- `[delete /api/mechanicalActions/{id}](about:blank#delete3)`
- `[get /api/mechanicalActions](about:blank#findAll3)`
- `[get /api/mechanicalActions/{id}](about:blank#findById3)`
- `[post /api/mechanicalActions](about:blank#save2)`
- `[put /api/mechanicalActions/{id}](about:blank#update2)`

### [OpenDay](about:blank#OpenDay)

- `[delete /api/opendays/{id}](about:blank#delete2)`
- `[get /api/opendays](about:blank#findAll2)`
- `[get /api/opendays/garage/{id}](about:blank#findByGarageId)`
- `[get /api/opendays/{id}](about:blank#findById2)`
- `[post /api/opendays/filterDays](about:blank#findFilterByDays)`
- `[post /api/opendays](about:blank#save1)`
- `[put /api/opendays/{id}](about:blank#update1)`

### [Place](about:blank#Place)

- `[delete /api/places/{id}](about:blank#delete1)`
- `[get /api/places](about:blank#findAll1)`
- `[get /api/places/{id}](about:blank#findById1)`
- `[get /api/places/findPlaceByMunicipalityStartsWith](about:blank#findPlaceByMunicipalityStartsWith)`
- `[get /api/places/findPlaceByProvinceStartsWith](about:blank#findPlaceByProvinceStartsWith)`
- `[get /api/places/findPlaceByRegionStartsWith](about:blank#findPlaceByRegionStartsWith)`

### [PublicController](about:blank#PublicController)

- `[get /api/public/populate](about:blank#populate)`
- `[get /api/public/test](about:blank#test)`
- `[get /api/public/testAddToCalendar](about:blank#testGoogle)`
- `[get /api/public/testRemoveFromCalendar](about:blank#testRemoveFromCalendar)`

### [UserController](about:blank#UserController)

- `[post /api/user/changePassword](about:blank#changePassword)`
- `[get /api/user/me](about:blank#getCurrentUser)`

### [Vehicle](about:blank#Vehicle)

- `[delete /api/vehicles/{id}](about:blank#delete)`
- `[get /api/vehicles](about:blank#findAll)`
- `[get /api/vehicles/{id}](about:blank#findById)`
- `[get /api/vehicles/user/{id}](about:blank#findByUserId)`
- `[post /api/vehicles](about:blank#save)`
- `[put /api/vehicles/{id}](about:blank#update)`

Ogni API e’ stata opportunamente commentata secondo gli standard di OpenAPI v3.0.1 , Commentando tutte le parti cruciali e gli esempi, con lo scopo di produrre una documentazione efficace.

---

*Esempio di definizione di un API:*

`post /api/appointments/{appointmentId}/state/{status}`

*Update the appointment state (appointmentStateUpdate). The response is the updated Appointment object. The status can be CONFIRMED, FINISHED, REJECTED*

### Path parameters

- status (required)
- appointmentId (required)

### Return type [Appointment](about:blank#Appointment)

### Example data

Content-Type: application/json

```
{
  "price" : 5.025004791520295,
  "idCalendarEvent" : "idCalendarEvent",
  "comment" : "comment",
  "votes" : [ null, null ],
  "startTime" : {
    "hour" : 9,
    "nano" : 8,
    "minute" : 9,
    "second" : 6
  },
  "id" : 1,
  "mechanicalAction" : {
    "price" : 1.4894159098541704,
    "name" : "name",
    "description" : "description",
    "id" : 1,
    "isActive" : true,
    "internalDuration" : {
      "zero" : true,
      "seconds" : 6,
      "negative" : true,
      "nano" : 7,
      "units" : [ {
        "duration" : {
          "zero" : true,
          "seconds" : 1,
          "negative" : true,
          "nano" : 4
        },
        "durationEstimated" : true,
        "timeBased" : true,
        "dateBased" : true
      }, {
        "duration" : {
          "zero" : true,
          "seconds" : 1,
          "negative" : true,
          "nano" : 4
        },
        "durationEstimated" : true,
        "timeBased" : true,
        "dateBased" : true
      } ]
    }
  },
  "externalTime" : {
    "start" : {
      "hour" : 10,
      "nano" : 0,
      "minute" : 30,
      "second" : 0
    }
  },
  "isMechanicalActionCustom" : true,
  "status" : "AWAITING_APPROVAL"
}
```

### Produces

This API call produces the following media types according to the Accept request header; the media type will be conveyed by the Content-Type response header.

- `application/json`

### Responses

### 200

OK

[Appointment](about:blank#Appointment)

### 404

Not Found

### 500

Internal Server Error

---

Tutte le API sono riportate e testate anche grazie a **Postman** , la cui collection verra’ allegata all’elaborato.

[Mechanical_Appointment.postman_collection.json](Mechanical%20Appointment%2039a8ed246b424c1ba0c079d2e0b08fdb/Mechanical_Appointment.postman_collection.json)

La documentazione e’ stata esportata in formato pdf e verra’ allegata all’elaborato, per ridurre il numero di pagina della relazione principale.

[api-documentation.pdf](Mechanical%20Appointment%2039a8ed246b424c1ba0c079d2e0b08fdb/api-documentation.pdf)

Swagger e’ accessibile al link : 

[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

---

## 8. Security

Spring Security è un framework del progetto Spring che consente di gestire in modo semplice e trasparente l’autenticazione (ovvero chi sei) e la profilazione (ovvero cosa sei autorizzato a fare) degli utenti che accedono ad una applicazione web.

API implementate:

| POST | /api/auth/creaUtenteIniziale | Inizializza il db e crea il primo utente admin , verra’ eliminato in seguito |
| --- | --- | --- |
| POST | /api/auth/refreshToken | Riceve come paramentro il vecchio refreshToken |
| GET | /api/user/me | Restituisce i dati base dell’utente |
| GET | /api/auth/tokenResetPassword | passera’ come paramentro il token che e’ stato ricevuto via email, come response ottiene il JWT |
| POST | /api/auth/recoveryPassword | Come paramentro mandare il campo email (/recoveryPassword?email=email@email.com) |
| POST | /api/user/changePassword | mandare nel body il campo newPassword + Authentication con JWT |
| GET | /api/aut/session | Fornendo la classica authentication, vengono restituite tutte le informazioni di login e sessione |

![package.jpg](Mechanical%20Appointment%2039a8ed246b424c1ba0c079d2e0b08fdb/package.jpg)

### Start & Configurazione

Il progetto prevede 3 file di configurazione

- *application.properties*
- *application-dev.properties*
- *application-prod.properties*

Il file di configurazione di default e’ *application.properties*, deve quindi contenere tutti i parametri di configurazione comuni. 

E’ possibile scegliere il profilo da utilizzare specificando nel file *application.properties*:

`spring.profiles.active=prod`

1. Creare il database MySQL
    
    mysql> create database `spring_security`
    
2. Configurare username e password del database 

```java
spring.datasource.url=jdbc:mysql://localhost:3306/spring_security?useSSL=false&serverTimezone=UTC&useLegacyDatetimeCode=false
spring.datasource.username=root
spring.datasource.password=admin
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL5InnoDBDialect
```

1. Aggiungere i dati di OAuth2 Provider ClientId's e ClientSecrets

```java
spring.security.oauth2.client.registration.google.clientId= ...
spring.security.oauth2.client.registration.google.clientSecret= ... 
spring.security.oauth2.client.registration.google.redirectUri=${beUrl}/oauth2/callback/{registrationId}
spring.security.oauth2.client.registration.google.scope=email, profile
spring.security.oauth2.client.provider.google.authorizationUri=https://accounts.google.com/o/oauth2/v2/auth?prompt=consent&access_type=offline
spring.security.oauth2.client.registration.google.authorization-grant-type=authorization_code
```

1. Configurare redirect Uri su Google developer console

Assicurati che `http://localhost:8080/oauth2/callback/<provider>`sia aggiunto come uri di reindirizzamento autorizzato nel provider OAuth2. Ad esempio, nella tua [console API di Google](https://console.developers.google.com/projectselector/apis/credentials?pli=1), assicurati che `http://localhost:8080/oauth2/callback/ google` viene aggiunto negli URI di reindirizzamento autorizzato.

Inoltre, assicurati che gli scope siano aggiunti nella console del provider OAuth2. Ad esempio, gli ambiti `email` e `profile` dovrebbero essere aggiunti nella schermata di consenso OAuth2 del tuo progetto Google.

1. Configurare le credenziali per invio email 

```java
mail.username= ...
mail.password= ...
spring.mail.host= ...
spring.mail.port= ...
spring.mail.username= ..
spring.mail.password= ...
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

Dettaglio del flusso di responsabilita’ sulla sicurezza : 

Il client invia una richiesta all'applicazione e il container crea un `FilterChain`, che contiene le istanze `Filter` e `Servlet` le quali dovranno elaborare il `HttpServletRequest`, in base al percorso dell'URI della richiesta.

![Untitled](Mechanical%20Appointment%2039a8ed246b424c1ba0c079d2e0b08fdb/Untitled%208.png)

`FilterChainProxy`è uno speciale `Filter`fornito da Spring Security che consente di delegare a molte `Filter`istanze tramite `[SecurityFilterChain](https://docs.spring.io/spring-security/reference/servlet/architecture.html#servlet-securityfilterchain)`.

`[SecurityFilterChain](https://docs.spring.io/spring-security/site/docs/6.0.2/api/org/springframework/security/web/SecurityFilterChain.html)` viene utilizzato da FilterChainProxy per determinare quali `Filter`istanze di Spring Security devono essere richiamate per la richiesta corrente.

![Untitled](Mechanical%20Appointment%2039a8ed246b424c1ba0c079d2e0b08fdb/Untitled%209.png)

### **Servlet Authentication Architecture**

Al centro del modello di autenticazione di Spring Security c'è il `SecurityContextHolder`. Contiene il SecurityContext .

![Untitled](Mechanical%20Appointment%2039a8ed246b424c1ba0c079d2e0b08fdb/Untitled%2010.png)

L’ interfaccia  `[Authentication](https://docs.spring.io/spring-security/site/docs/6.0.2/api/org/springframework/security/core/Authentication.html)`ha due scopi principali :

- Input per `[AuthenticationManager](https://docs.spring.io/spring-security/reference/servlet/authentication/architecture.html#servlet-authentication-authenticationmanager)` e fornire le credenziali fornite da un utente per l'autenticazione.
    
    Quando viene utilizzato in questo scenario, `isAuthenticated()`restituisce `false`.
    
- Rappresenta l'utente attualmente autenticato. È possibile ottenere la corrente `Authentication`da [SecurityContext](https://docs.spring.io/spring-security/reference/servlet/authentication/architecture.html#servlet-authentication-securitycontext) .

l’ `Authentication`contiene:

- `principal`: identifica l'utente. Quando ci si autentica con un nome utente/password, spesso si tratta di un'istanza di `[UserDetails](https://docs.spring.io/spring-security/reference/servlet/authentication/passwords/user-details.html#servlet-authentication-userdetails)`.
- `credentials`: Spesso una password. In molti casi, questo viene cancellato dopo che l'utente è stato autenticato, per garantire la sicurezza
- `authorities`: le `[GrantedAuthority](https://docs.spring.io/spring-security/reference/servlet/authentication/architecture.html#servlet-authentication-granted-authority)` sono autorizzazioni di alto livello concesse all'utente. Due esempi sono i ruoli e gli ambiti.

Di solito, le `GrantedAuthority` sono autorizzazioni a livello di applicazione. Non sono specifici di un determinato oggetto di dominio.

`[AuthenticationManager](https://docs.spring.io/spring-security/site/docs/6.0.2/api/org/springframework/security/authentication/AuthenticationManager.html)`è l'API che definisce come i filtri di Spring Security eseguono [l'autenticazione](https://docs.spring.io/spring-security/reference/features/authentication/index.html#authentication) . 

L’ `[Authentication](https://docs.spring.io/spring-security/reference/servlet/authentication/architecture.html#servlet-authentication-authentication)`che viene restituito viene quindi impostato su [SecurityContextHolder](https://docs.spring.io/spring-security/reference/servlet/authentication/architecture.html#servlet-authentication-securitycontextholder) dal controller (ovvero dalle [istanze di Spring Security`Filters`](https://docs.spring.io/spring-security/reference/servlet/authentication/architecture.html#servlet-security-filters) ) che ha richiamato il `AuthenticationManager`. 

le istanze di Spring Security, possono impostare `SecurityContextHolder`direttamente e non è necessario utilizzare un file `AuthenticationManager`.

`[ProviderManager](https://docs.spring.io/spring-security/site/docs/6.0.2/api/org/springframework/security/authentication/ProviderManager.html)`è l'implementazione più comunemente usata di `[AuthenticationManager](https://docs.spring.io/spring-security/reference/servlet/authentication/architecture.html#servlet-authentication-authenticationmanager)`. 

Ogni `AuthenticationProvider`ha l'opportunità di indicare che l'autenticazione dovrebbe avere successo, fallire o indicare che non può prendere una decisione e consentire a un downstream `AuthenticationProvider`di decidere

### **AbstractAuthenticationProcessingFilter**

![Untitled](Mechanical%20Appointment%2039a8ed246b424c1ba0c079d2e0b08fdb/Untitled%2011.png)

1. Quando l'utente invia le proprie credenziali, `AbstractAuthenticationProcessingFilter`crea un `[Authentication](https://docs.spring.io/spring-security/reference/servlet/authentication/architecture.html#servlet-authentication-authentication)`from da `HttpServletRequest`. Il tipo di `Authentication`creato dipende dalla sottoclasse di `AbstractAuthenticationProcessingFilter`. Ad esempio, `[UsernamePasswordAuthenticationFilter](https://docs.spring.io/spring-security/reference/servlet/authentication/passwords/form.html#servlet-authentication-usernamepasswordauthenticationfilter)`crea un `UsernamePasswordAuthenticationToken`da un *nome utente* e *una password* inviati nel file `HttpServletRequest`.
2. Successivamente, `[Authentication](https://docs.spring.io/spring-security/reference/servlet/authentication/architecture.html#servlet-authentication-authentication)`viene passato al file `[AuthenticationManager](https://docs.spring.io/spring-security/reference/servlet/authentication/architecture.html#servlet-authentication-authenticationmanager)`da autenticare.
3. Se l'autenticazione fallisce, *Failure* .
- Il SecurityContextHolder viene cancellato.
- `RememberMeServices.loginFail`viene invocato. Se ricordati di me non è configurato, non è possibile.
- `AuthenticationFailureHandler`viene invocato. Vedi l' `[AuthenticationFailureHandler](https://docs.spring.io/spring-security/site/docs/6.0.2/api/org/springframework/security/web/authentication/AuthenticationFailureHandler.html)`interfaccia.
1. Se l'autenticazione ha esito positivo:
- `SessionAuthenticationStrategy`viene avvisato di un nuovo accesso.
- L' autenticazione è impostata su SecurityContextHolder . Successivamente, `SecurityContextPersistenceFilter`salva il file `SecurityContext`nel file `HttpSession`.
- `RememberMeServices.loginSuccess`viene invocato. Se remember me non è configurato, non è possibile. Vedi il `[rememberme](https://docs.spring.io/spring-security/site/docs/6.0.2/api/org/springframework/security/web/authentication/rememberme/package-frame.html)` pacchetto.
- `ApplicationEventPublisher`pubblica un `InteractiveAuthenticationSuccessEvent`.
- `AuthenticationSuccessHandler`viene invocato.

### **Flusso per refresh token Spring Boot con JWT**

Il diagramma mostra il flusso di come implementiamo il processo di autenticazione con token di accesso e token di aggiornamento.

![Untitled](Mechanical%20Appointment%2039a8ed246b424c1ba0c079d2e0b08fdb/Untitled%2012.png)

`refreshToken`verrà fornito al momento dell'accesso dell'utente.

Il token di aggiornamento ha valore e tempo di scadenza diversi rispetto al token di accesso.

Nel metodo del`refreshtoken()`:

- In primo luogo, otteniamo il token di aggiornamento dai dati della richiesta
- Quindi, ottengo l’oggetto di refresh dal token utilizzando `RefreshTokenService`
- Verifichiamo il token (scaduto o meno) in base al campo`expiryDate`
- Utilizziamo il campo `userRefreshTokenJwtUtils`dell'oggetto come parametro per generare un nuovo token di accesso, che verra’ salvato a DB tramite la repository.
- Quando viene creato un nuovo refresh token, il service `RefreshTokenService`
- Restituisco `TokenRefreshResponse` Oppure lancia`TokenRefreshException`

Un thread task ogni 5 minuii elimina i refreshToken da database piu’ vecchi dell’expirationDate

```java
refreshTokenRepository.deleteByExpiryDateIsLessThan(Instant.now().plusMillis(Long.parseLong(Objects.requireNonNull( env.getProperty("app.auth.refreshTokenExpiration"))) + 1000));
```

### **Flusso per recupero password**

Per recuperare la password l’untente potra’ ricevere una email fornendo la propria, se la registrazione e’ avvenuta attraverso OAuth2 dovra’ eseguire l’accesso con quel provider.

Per richiedere la mail con la nuova password , fornendo come parametro la mail:

![Untitled](Mechanical%20Appointment%2039a8ed246b424c1ba0c079d2e0b08fdb/Untitled%2013.png)

Cliccando sul link verrà fatto il redirect ad una pagina FE, che richiedera’ il JWT di sessione attraverso :

L’utente ricevera’ una mail con il collegamento ed il token per effettuare il recupero della password:

```json
http://localhost:8080/api/auth/recoveryPassword?email=mtempobono@gmail.com
```

in cui passera’ come parametro il token che e’ stato ricevuto via email, come response ottiene il JWT

Per il reset della password aggiungere nel body “newPassword” 

```json
http://localhost:8080/api/user/changePassword
```

---

## 9. Email per comunicazioni e Google Calendar

A seguito della conferma degli appuntamenti, vengono inviate delle email per interagire con i clienti , ed aggiornarli sullo stato delle loro prenotazioni :

1. Creazione di un appuntamento 

A seguito dell’invio di un nuovo appuntamento, viene inviata una email di riepilogo :

![Untitled](Mechanical%20Appointment%2039a8ed246b424c1ba0c079d2e0b08fdb/Untitled%2014.png)

1. Tutti gli appuntamenti , sia CUSTOM che STANDARD devono essere opportunamente approvati dal meccanico , verranno inviate delle email in caso di conferma di un nuovo appuntamento oppure in caso di rifiuto :

![Untitled](Mechanical%20Appointment%2039a8ed246b424c1ba0c079d2e0b08fdb/Untitled%2015.png)

![Untitled](Mechanical%20Appointment%2039a8ed246b424c1ba0c079d2e0b08fdb/Untitled%2016.png)

1. Dopo il verificarsi dell’appuntamento, verra’ inviato una email contenente il riepilogo dell’intervento, come ricevuta

![Untitled](Mechanical%20Appointment%2039a8ed246b424c1ba0c079d2e0b08fdb/Untitled%2017.png)

In questa email viene mandato il collegamento alla pagina per effettuare la recensione.

Sul calendario del meccanico verranno aggiunti gli eventi per i vari OpenDay automaticamente.

Il calendario puo’ essere condiviso con il team essendo online su google calendar 

![Untitled](Mechanical%20Appointment%2039a8ed246b424c1ba0c079d2e0b08fdb/Untitled%2018.png)

---

## 10. Testing

Il testing e’ stato realizzato utilizzando JUnit. Junit e’ un framework di test open source per Java con supporto di un test runner per eseguire i test case. Fornisce Assertion di supporto per il controllo del risultato atteso.

Per eseguire test unitari, sono stati creati casi di test. Il **test unitario** è un codice che garantisce che la logica del programma funzioni come previsto.

### **Annotazioni per i test Junit**

Il framework Junit è basato sulle annotazioni, possono essere utilizzate durante la scrittura dei casi di test:

**L'annotazione @Test** specifica che il metodo è il metodo di prova.

**L'annotazione @Test(timeout=1000)** specifica che il metodo avrà esito negativo se impiega più di 1000 millisecondi (1 secondo).

**L'annotazione @BeforeClass** specifica che il metodo verrà invocato solo una volta, prima di iniziare tutti i test.

**L'annotazione @Before** specifica che il metodo verrà richiamato prima di ogni test.

**L'annotazione @After** specifica che il metodo verrà richiamato dopo ogni test.

**L'annotazione @AfterClass** specifica che il metodo verrà invocato solo una volta, dopo aver terminato tutti i test.

La classe *org.junit.Assert* fornisce metodi per asserire la logica del programma.

### **Metodi della classe Assert**

I metodi comuni della classe Assert sono i seguenti:

1. **void assertEquals(boolean expected,boolean actual)**
    
    : verifica che due primitive/oggetti siano uguali. È sovraccarico.
    
2. **void assertTrue(condizione booleana)**
    
    : verifica che una condizione sia vera.
    
3. **void assertFalse(condizione booleana)**
    
    : verifica che una condizione sia falsa.
    
4. **void assertNull(Object obj)**
    
    : verifica che l'oggetto sia nullo.
    
5. **void assertNotNull(Object obj)**
    
    : verifica che l'oggetto non sia nullo.
    

Per creare dei casi di test complessi , viene utilizzato **H2** , il quale contiene dei dati di esempio su cui eseguire i test.

H2 è un database integrato **,** open source **e** in memoria. È un sistema di gestione di database relazionali scritto in Java.  Viene generalmente utilizzato per i test di integrazione. Memorizza i dati in memoria, non persiste i dati su disco.

I dati di esempio sono stati scritti in un file data_init.sql , il quale contiene la struttura dei dati SQL con le query di creazione , ad esempio: 

```bash
CREATE MEMORY TABLE "PUBLIC"."APPOINTMENT"(
    "ID" BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1 RESTART WITH 2) NOT NULL,
    "COMMENT" CHARACTER VARYING(255),
    "EXTERNAL_TIME" CHARACTER VARYING(255),
    "ID_CALENDAR_EVENT" CHARACTER VARYING(255),
    "INTERNAL_TIME" CHARACTER VARYING(255),
    "IS_MECHANICAL_ACTION_CUSTOM" BOOLEAN,
    "PRICE" DOUBLE PRECISION,
    "STATUS" CHARACTER VARYING(255),
    "MECHANICAL_ACTION_ID" BIGINT,
    "OPEN_DAY_ID" BIGINT,
    "VEHICLE_ID" BIGINT
);           
ALTER TABLE "PUBLIC"."APPOINTMENT" ADD CONSTRAINT "PUBLIC"."CONSTRAINT_2" PRIMARY KEY("ID");  
-- 1 +/- SELECT COUNT(*) FROM PUBLIC.APPOINTMENT;             
INSERT INTO "PUBLIC"."APPOINTMENT" VALUES
(1, 'I need to change the tires', '{"start":{"hour":0,"minute":0,"second":0,"nano":0},"end":{"hour":0,"minute":0,"second":0,"nano":0}}', NULL, '{"start":{"hour":0,"minute":0,"second":0,"nano":0},"end":{"hour":0,"minute":0,"second":0,"nano":0}}', true, 0.0, 'AWAITING_APPROVAL', 1, 1, 1);
```

Utilizziamo l'annotazione @SpringBootTest per avviare l'intero contenitore. L'annotazione funziona creando gli ApplicationContext che saranno utilizzati nei test.
Quindi applicando @AutoConfigureMockMvc nella classe di test per abilitare e configurare la configurazione automatica di Mock-Mvc.
Infine, applichiamo @DisplayNameGeneration per dichiarare un generatore di nomi di visualizzazione personalizzato per la classe di test annotata.

@Sql è usato per eseguire le query SQL presenti nel classpath.
Per ogni metodo da eseguire, si possono fornire le annotazioni @SqlGroup e @Sql per eseguire gli script prima di eseguire il test vero e proprio.
In questo caso, verifichiamo che la query restituisca tutti gli utenti presenti nel database e, grazie a MockMvcResultMatchers.jsonPath, possiamo verificare la risposta della nostra richiesta e validare che gli id siano quelli aspettati in ordine crescente.

```bash
@Test
@SqlGroup({
    @Sql(value = "classpath:empty/reset.sql", executionPhase = BEFORE_TEST_METHOD),
    @Sql(value = "classpath:init/user-data.sql", executionPhase = BEFORE_TEST_METHOD)
})
```

Riportando alcuni dei casi di test piu’ complessi del `AppointmentServiceImplTest`: 

1. getAvailableHoursWithMaxTwoParallelAppointments

Analizzando il seguente calendario : 

Il lavoro da eseguire ha una durata interna di 1 ora ed una durata esterna di 2 ore.

| 11:00 → 11:30 |  |  |  |  |
| --- | --- | --- | --- | --- |
| 11:30 → 12:00 |  |  |  |  |
| 12:00 → 14:00 | x | x | x | x |
| 14:00 → 14:30 | x |  |  |  |
| 14:30 → 15:00 | x |  |  | x |
| 15:00 → 15:30 | x | x |  | x |
| 15:30 → 16:00 | x | x | x |  |
| 16:00 → 16:30 |  | x | x |  |
| 16:30 → 17:00 |  |  | x |  |

Le aspettative ,Basandoci sul tempo interno, sono :

- Appuntamento disponibile dalle 11-12
- Appuntamento disponibile dalle  14-15
- Appuntamento disponibile dalle 16 - 17

Basandoci sul tempo esterno 

- Appuntamento disponibile dalle  11 -13
- Appuntamento disponibile dalle  14-16
1. getAvailableHoursWithMaxOneParallelAppointments1

Il lavoro da eseguire ha una durata interna di 1 ora ed una durata esterna di 6 ore.

| 6:00 |  |  |  |
| --- | --- | --- | --- |
| 7:00 → 7:30 |  |  |  |
| 7:30 → 8:00 |  |  |  |
| 8:00 → 8:30 | x |  |  |
| 8:30 → 9:00 | x |  |  |
| 9:00 →9:30 | x |  |  |
| 9:30 → 10:00 | x |  |  |
| 10:00 → 10:30 | x |  |  |
| 10:30 → 11:00 | x |  |  |
| 11:00 → 11:30 |  |  |  |
| 11:30 → 12:00 |  |  |  |
| 12:00 → 14:00 | x | x | x |
| 14:00 → 14:30 |  |  |  |
| 14:30 → 15:00 |  |  |  |
| 15:00 → 15:30 | x |  |  |
| 15:30 → 16:00 | x |  |  |
| 16:00 → 16:30 | x |  |  |
| 16:30 → 17:00 |  |  |  |

IL seguente caso di test rileva 9 slot disponibili basandoci sull’orario interno e 5 slot basandoci sull’orario esterno.

1. getAvailableHoursWithMaxOneParallelAppointments2

| 6:00 |  |  |  |
| --- | --- | --- | --- |
| 7:00 → 7:30 |  |  |  |
| 7:30 → 8:00 |  |  |  |
| 8:00 → 8:30 | x |  |  |
| 8:30 → 9:00 | x |  |  |
| 9:00 →9:30 | x |  |  |
| 9:30 → 10:00 | x |  |  |
| 10:00 → 10:30 | x | x |  |
| 10:30 → 11:00 | x | x |  |
| 11:00 → 11:30 |  | x |  |
| 11:30 → 12:00 |  | x |  |
| 12:00 → 14:00 | x | x |  |
| 14:00 → 14:30 |  |  |  |
| 14:30 → 15:00 |  |  |  |
| 15:00 → 15:30 | x |  |  |
| 15:30 → 16:00 | x |  |  |
| 16:00 → 16:30 | x |  |  |
| 16:30 → 17:00 |  |  |  |

Caso in condizioni analoghe al test 2, 

Gli slot che risulteranno occupati saranno 

- 6 - 12
- 7 - 13
- 8 - 14
- 9 - 15

1. getAvailableHoursWithMaxOneParallelAppointments3

Il lavoro da eseguire ha una durata interna di 30 minuti ed una durata esterna di 1 ora.

| 6:00 |  |  |
| --- | --- | --- |
| 7:00 → 7:30 |  |  |
| 7:30 → 8:00 |  |  |
| 8:00 → 8:30 | x |  |
| 8:30 → 9:00 | x |  |
| 9:00 →9:30 | x |  |
| 9:30 → 10:00 | x |  |
| 10:00 → 10:30 | x | x |
| 10:30 → 11:00 | x | x |
| 11:00 → 11:30 |  | x |
| 11:30 → 12:00 |  | x |
| 12:00 → 14:00 | x | x |
| 14:00 → 14:30 |  |  |
| 14:30 → 15:00 |  |  |
| 15:00 → 15:30 | x |  |
| 15:30 → 16:00 | x |  |
| 16:00 → 16:30 | x |  |
| 16:30 → 17:00 |  |  |

Ci aspettiamo che non siano disponibili le fasce orarie dalle 10 alle 10.30 e dalle 10.30 alle 11

Per quando riguarda i tempi esterni non saranno disponibili nemmeno le fasce 16.30 - 17 poiche’ l’orario del ritiro sarebbe oltre l’orario di chiusura

1. getAvailableHoursWithMaxOneParallelAppointments3

| 6:00 → 6:30 |  |  |
| --- | --- | --- |
| 6:30 →7:00 | x |  |
| 7:00 → 7:30 | x |  |
| 7:30 → 8:00 | x |  |
| 8:00 → 8:30 | x |  |
| 8:30 → 9:00 | x |  |
| 9:00 →9:30 | x |  |
| 9:30 → 10:00 | x |  |
| 10:00 → 10:30 | x |  |
| 10:30 → 11:00 |  |  |
| 11:00 → 11:30 |  |  |
| 11:30 → 12:00 |  |  |
| 12:00 → 14:00 | x | x |
| 14:00 → 14:30 |  |  |
| 14:30 → 15:00 |  |  |
| 15:00 → 15:30 |  |  |
| 15:30 → 16:00 |  |  |
| 16:00 → 16:30 |  |  |
| 16:30 → 17:00 |  |  |

Con un appuntamento da pianificare, dalla durata di 2 ore interne e 4 esterne dovremmo avere un solo slot disponibile basandoci sul tempo esterno e 3 disponibili sul tempo interno.

![Untitled](Mechanical%20Appointment%2039a8ed246b424c1ba0c079d2e0b08fdb/Untitled%2019.png)

Sono stati realizzati anche i test per tutte le api disponibili in piattaforma : 

- AppointmentControllerTest

![Untitled](Mechanical%20Appointment%2039a8ed246b424c1ba0c079d2e0b08fdb/Untitled%2020.png)

- AuthControllerTest

![Untitled](Mechanical%20Appointment%2039a8ed246b424c1ba0c079d2e0b08fdb/Untitled%2021.png)

- GarageControllerTest

![Untitled](Mechanical%20Appointment%2039a8ed246b424c1ba0c079d2e0b08fdb/Untitled%2022.png)

- MechanicalActionsControllerTest

![Untitled](Mechanical%20Appointment%2039a8ed246b424c1ba0c079d2e0b08fdb/Untitled%2023.png)

- OpenDaysControllerTest

![Untitled](Mechanical%20Appointment%2039a8ed246b424c1ba0c079d2e0b08fdb/Untitled%2024.png)

- PlacesControllerTest

![Untitled](Mechanical%20Appointment%2039a8ed246b424c1ba0c079d2e0b08fdb/Untitled%2025.png)

- VehiclesControllerTest

In ultima fase riporto Il testing con casi di test negativi.

E’ un tipo di test del software utilizzato per verificare che l'applicazione software non presenti dati e condizioni di input inattesi. I dati o le condizioni inattese possono essere di qualsiasi tipo e lo scopo del test negativo è quello di evitare che l'applicazione software si blocchi a causa di input negativi e di migliorare la qualità e la stabilità.

Eseguendo solo test positivi possiamo solo assicurarci che il nostro sistema funzioni in condizioni normali. Dobbiamo assicurarci che il nostro sistema sia in grado di gestire condizioni inaspettate per garantire un sistema privo di errori al 100%.

Per fare un test negativo ho considerato alcuni Test Case, indipendentemente dal fatto che non sia il modo giusto di usarlo. 

Ho valutato quindi i principali tipi di Exception sollevate dal softaware:

- IllegalArgumentException
- AssertionError
- FileNotFoundException
- Exception
- NullPointerException
- ClassCastException

![Untitled](Mechanical%20Appointment%2039a8ed246b424c1ba0c079d2e0b08fdb/Untitled%2026.png)

I test totali sono 59 ed hanno portato ad una coverage considerevole sull’intero progetto : 

![Untitled](Mechanical%20Appointment%2039a8ed246b424c1ba0c079d2e0b08fdb/Untitled%2027.png)

### Coverage Summary

| Package | Class, % | Method, % | Line, % |
| --- | --- | --- | --- |
| all classes | 79.4%   (85/107) | 71.2%   (636/893) | 56.6%   (1497/2647) |

### Coverage Breakdown

| https://www.notion.soindex_SORT_BY_NAME_DESC.html | https://www.notion.soindex_SORT_BY_CLASS.html | https://www.notion.soindex_SORT_BY_METHOD.html | https://www.notion.soindex_SORT_BY_LINE.html |
| --- | --- | --- | --- |
| https://www.notion.sons-1/index.html | 100%   (1/1) | 100%   (3/3) | 100%   (3/3) |
| https://www.notion.sons-2/index.html | 87.5%   (7/8) | 63.4%   (26/41) | 78.5%   (102/130) |
| https://www.notion.sons-3/index.html | 100%   (11/11) | 75.3%   (61/81) | 42.5%   (94/221) |
| https://www.notion.sons-5/index.html | 100%   (3/3) | 90%   (27/30) | 81.6%   (62/76) |
| https://www.notion.sons-6/index.html | 77.8%   (7/9) | 74.5%   (70/94) | 78.1%   (185/237) |
| https://www.notion.sons-7/index.html | 80%   (16/20) | 75.1%   (151/201) | 55.1%   (157/285) |
| https://www.notion.sons-8/index.html | 92.9%   (13/14) | 74.2%   (158/213) | 55.8%   (178/319) |
| https://www.notion.sons-9/index.html | 100%   (4/4) | 73.7%   (14/19) | 42.9%   (54/126) |
| https://www.notion.sons-a/index.html | 100%   (4/4) | 22.2%   (4/18) | 7.7%   (7/91) |
| https://www.notion.sons-c/index.html | 91.7%   (11/12) | 78.2%   (86/110) | 59.2%   (583/984) |
| https://www.notion.sons-d/index.html | 50%   (3/6) | 33.3%   (7/21) | 38.1%   (24/63) |
| https://www.notion.sons-e/index.html | 100%   (3/3) | 100%   (11/11) | 62.5%   (20/32) |
| https://www.notion.sons-f/index.html | 66.7%   (2/3) | 78.3%   (18/23) | 77.8%   (28/36) |

---

## 11. Docker e build del progetto

Docker è uno strumento che semplifica la creazione, distribuzione ed esecuzione di applicazioni utilizzando i container.

I container permettono di creare un pacchetto completo di un'applicazione e tutte le sue dipendenze, distribuendolo come un unico pacchetto.

In questo modo, l'applicazione può essere eseguita su una macchina Linux. 

Grazie a Docker, è possibile creare un container con l'applicativo e utilizzare un file YAML per configurare i servizi per l'applicazione.

È possibile definire l'ambiente dell'applicazione tramite un Dockerfile, in modo che possa essere riprodotto ovunque. 

```bash
FROM openjdk:17-oracle
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]
```

La prima riga "FROM openjdk:17-oracle" indica l'immagine di base con cui creare la nuova immagine docker. In questo caso, viene utilizzata l'immagine openjdk:17-oracle, che fornisce una versione dell'ambiente di runtime Java.

La seconda riga "ARG JAR_FILE=target/*.jar" rappresenta il nome del file jar dell'applicazione. Il valore di questo argomento viene specificato quando si avvia il comando "docker build" per creare l'immagine Docker.

La terza riga "COPY ${JAR_FILE} app.jar" copia il file jar dell'applicazione dall'host alla nuova immagine Docker. l'argomento JAR_FILE viene utilizzato per indicare il file jar dell'applicazione nella cartella "target" del progetto.

La quarta riga "EXPOSE 8080" definisce la porta sulla quale l'applicazione in esecuzione ascolterà le richieste. In questo caso, viene esposta la porta 8080.

Infine, la quinta riga "ENTRYPOINT ["java","-jar","/app.jar"]" definisce il comando di avvio dell'applicazione quando il container viene avviato. 

Viene utilizzato il comando "java -jar /app.jar" per avviare l'applicazione in esecuzione all'interno del container Docker.

Nel caso in cui non si volesse utilizzare docker per effettuare il deploy dell’applicativo ,per eseguire un progetto Maven :

1. apri prompt dei comandi.
2. Naviga nella directory del progetto Maven. 
3. Esegui il comando Maven **`mvn clean install`**. Questo comando compila il tuo , esegue i test e genera il pacchetto JAR.
4. Dopo aver compilato il progetto, e’ possibile eseguirlo utilizzando il comando **`java -jar <nome-del-file-jar>`**. Ad esempio,  "mechanicalAppointment.jar", digita 
    
    **`java -jar** mechanicalAppointment**.jar**.`