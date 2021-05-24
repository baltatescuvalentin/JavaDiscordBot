  # JavaDiscordBot
  Discord Bot facut cu JDA(Java Discord API) de Baltatescu Valentin-Constantin.

  Acest bot este creat astfel incat sa primeasca comenzi in chat si daca acesta stie sa le trateze va oferi anumite output-uri. 
  Comenzile date bot-ului trebuie sa aiba un format specific astfel incat acesta sa le inteleaga. Pentru aceasta user-ul va trimite un mesaj de forma !comanda argument1..n in functie de tipul comenzii.
  Comenzile existente sunt:
   ![image](https://user-images.githubusercontent.com/75161284/119339527-ee5ca800-bc99-11eb-8bc1-c544c98df2c1.png)
   
   ![image](https://user-images.githubusercontent.com/75161284/119339542-f3b9f280-bc99-11eb-9831-9934c3cbce15.png)
   
  Aceste comenzi sunt salvate cu ajutorul unei metode care le stocheaza intr-un ArrayList si se asigura de faptul ca nu va fi adaugat un duplicat la aceasta. Prin intermediul comenzii !help vor fi afisate comenzile existente si descrierea lor cum e in imaginile de mai sus.
  Mesajele preluate de bot sunt tratate cu ajutorul unui "listener" care preia mesajul ca si un eveniment de tipul GuildMessageReceivedEvent care este impartit intr-o lista ca sa fie prelucrat cum este nevoie in fiecare dintre comenzi.
  Fiecare dintre clasele care va trata o anumita comanda va fi nevoita sa implementeze anumite metode: getHelp, getName, handle fapt garantat ca acestea implementeaza o interfata. Aceste metode sunt importante deoarece cu ajutorul lor preluam numele comenzii din clasa, descrierea acesteia si tratarea comenzii pentru ca aceasta sa realizeze actiunea dorita.
  Functiile necesare proiectului sunt cea de raspuns la intrebari simple si preluarea de mesaje prin intermediul RSS-urilor pe teme de programare.
  Pentru ca bot-ul sa raspunda la intrebari simple trebuie folosita sintaxa !question <intrebare>. Modul prin care bot-ul reuseste sa raspunda la intrebari este ca exista un set de intrebari prestabilite salvate intr-o baza de date facuta cu ajutorl MySQL. Odata ce un utilizator apeleaza comanda programul filtreaza mesajul si preaia doar intrebarea, dupa care se va accesa baza de date si se va compara intrebarea cu campul <question> si daca aceasta exista atunci se va returna campul <answer> si va aparea in chat. In cazul in care nu exista aceasta intrebare bot-ul nu va sti sa raspunda si va trimite un mesaj prestabilit.
  
  ![image](https://user-images.githubusercontent.com/75161284/119341861-f1a56300-bc9c-11eb-8438-7be6188e6e00.png)
  
  Articolele legate de programare vor fi preluate prin ajutorul librariei ROME care este de tip RSS(Rich Site Summar sau Really Simple Syndication). Acesta le-am gandit ca in cazul unui "feed" al unui site care trimite cele mai mai actualizari/postari alea sale. Pentru preluarea acestor "feed-uri" am mai multe URL-uri specifice pentru aceasta, exemplu: http://rss.cnn.com/rss/edition_world.rss ,acestea fiind salvate intr-un fisier XML salvate sub forma <subject> care este topicul pe care il dorim,exemplu "news", si <url> care este URL-ul catre site-ul specific. Prin intermediul libreriei ROME se preia codul sursa al paginii dupa care luam doar ce ne intereseaza din acesta cum ar fi: titlul articolului, autorul si rezumatul. Totusi pentru a obtine text curat este nevoie sa filtram textul si sa scapam de tag-urile HTML, acest lucru realizat cu ajutorul JSOUP-ului care usureaza munca. Dupa care toate aceste informatii sunt puse intr-un "container" de tip Embed sub forma de sablon. Modul in care se apeleaza aceasta comanda este !feed <subiect> <numar_de_articole> si va afisa numarul de articole ceruta pentru acel subiect. Daca nu va fi respectat formatul se va trimite un mesaj cu subiectele existente.
  
  ![image](https://user-images.githubusercontent.com/75161284/119343519-3c27df00-bc9f-11eb-84e1-d672a376dd0d.png)
  
  A mai fost implementat si o modalitatea de a asculta muzica cu ajutor-ul bot-ului dar aceasta cuprinde mai multe componente. Prima ar fi libraria "lavaplayer" recomandata de developerii de la JDA care trebuie sa contina 4 clase specifice pentru a prelua link-ul de la platforma, sa realizeze conexiunea audio, sa realizeze coada pentru request-urile cu link-uri si sa transmita sunet audio. Totusi pentru a putea folosi aceste facilitati trebuie ca bot-ul sa se conecteze la un canal de "voce" unde acesta va pune melodiile, va gestiona coada si le va opri, acestea fiind: !join, !leave, !next, !play, !stop care se folosesc de clasele implementate continand libraria "lavaplayer". Aceste comenzi pot fi demonstrate doar utilizand bot-ul.
  O comanda pentru divertisment este cea de !meme care returneaza o gluma aleatoare preluata de pe un site. Aceasta gluma este preluata prin intermediul unui JSON care are anumite caracteristici, site-ul API-ului fiind acesta: https://meme-api.herokuapp.com/gimme . Astept preluand JSON-ul din API si luand caracteristicile care ne intereseaza adica titlul si URL-ul imaginii care sunt pune intr-un element Embed.
  
  ![image](https://user-images.githubusercontent.com/75161284/119344916-ebb18100-bca0-11eb-8b98-81d9b0e370ca.png)
  
  O alta comanda pentru amuzament este cea de !slots ,care simuleaza un aparat de jocuri de noroc. Aceasta comanda are la baza un algoritm bazat pe alegeri aleatoare a unor valori predefinite si castigurile se obtin prin obtinerea unei linii cu 3 elemente identice.
  
  ![image](https://user-images.githubusercontent.com/75161284/119345540-b194af00-bca1-11eb-9978-2b6ef438321f.png)
  
  Un feature care ar fi util pentru programatori pentru a-si pune la testare cunostintele este comanda de !quiz. Aceasta transmite o intrebare dintr-un set predefinit de intrebari salvate intr-o baza de date de tip MySQL fiind aleasa aleator si transmisa intr-un format de tip Embed. Dupa ce mesajul este transmis cu ajutorul unui EventWaiter bot-ul va astepta un raspuns la intrebare, astfel primul care trimite un mesaj va raspunde la intrebare si acel mesaj va fi prelucrat si validat daca este corect sau nu. Intrebarile in baza de date sunt salvate intr-un format <idquiz> <question> <answer1..4> <correct> <source>. <idquiz>-ul este util pentru a alege intrebarea in mod aleator si <source> este pentru cand cineva raspunde gresit la intrebare va primi o documentatie care sa fie util pentru a intelege ce a gresit.
  
  ![image](https://user-images.githubusercontent.com/75161284/119346612-1a305b80-bca3-11eb-9961-130d5ba82607.png)
  ![image](https://user-images.githubusercontent.com/75161284/119346664-2f0cef00-bca3-11eb-9560-d88bfa98d79a.png)
  ![image](https://user-images.githubusercontent.com/75161284/119346698-3b914780-bca3-11eb-927b-3830e8047b40.png)
  ![image](https://user-images.githubusercontent.com/75161284/119346757-4b109080-bca3-11eb-9652-f21e7bb8b2b0.png)
  
  O comanda utila pentru cei de pe server ar fi cea in care utilizatorii recomanda lucruri carora le plac, de exemplu carti/filme/seriale/muzica/jocuri. Aceasta are rolu de a impartasi cu ceilalti anumite placeri, a gasi oameni cu aceleasi gusturi sau sa gaseasca ceva nou. Aceste sugestii sunt salvate intr-o baza de date de tip MySQL avand cate un tabel pentru fiecare topic. Fiecare dintre acestea contin coloanele <nume> <gen> <nota> <recomandat> - acesta va fi user-ul care adauga o noua informatie in tabel. 
  Comanda are structura !recommand si difera in functie de ce operatie doreste sa fie facuta. Daca cineva vrea sa adauge ceva va fi de forma !recommand add <table> <genre> <grade> <name>, pentru a sterge !recommand remove <table> <nume> si show pentru a afisa !recommand show <table>. Toate operatiile de inserare/stergere/select sunt facute direct din Intellij cu ajutorul librarieri JDBC si care se realizeaza intructiunile DML. 
  
  ![image](https://user-images.githubusercontent.com/75161284/119347823-b7d85a80-bca4-11eb-840d-10fbf0e2121b.png)
  ![image](https://user-images.githubusercontent.com/75161284/119347946-e22a1800-bca4-11eb-8c4f-5d26be774a80.png)
  ![image](https://user-images.githubusercontent.com/75161284/119347983-f110ca80-bca4-11eb-8f86-0d4eddd95c0b.png)
  
  Comanda pentru a opri botul este !shudown care intrerupe legatura bot-ului cu server-ul de Discord si opreste si programul care ruleaza.








  
