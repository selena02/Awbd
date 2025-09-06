# Springboot e-commerce
## Descriere generală
Acest proiect a fost realizat în cadrul materiei Aplicații Web pentru Baze de Date și reprezintă implementarea unui sistem de e-commerce folosind arhitectura de microservicii.

Proiectul este construit în Spring Boot și utilizează MySQL pentru stocarea datelor. Am ales să împart aplicația în două microservicii independente:

User Service – gestionează autentificarea și înregistrarea utilizatorilor

Product Service – gestionează produsele disponibile în magazin

Am folosit Swagger UI pentru documentarea și testarea API-urilor într-un mod vizual și intuitiv.

## Tehnologii folosite

- Java 21
- Spring Boot
- Spring Data JPA
- MySQL
- Swagger (OpenAPI)
- Maven

## Structura proiectului

Proiectul conține două microservicii independente, fiecare având propria structură de tip Controller – Service – Repository – Model – DTO – Mapper – Exception – Config.

### Product Service

- controller – gestioneaza request-urile pentru produse si review-uri (ProductController, ReviewController)
<img width="1872" height="1560" alt="image" src="https://github.com/user-attachments/assets/706b82e8-37f4-4931-920a-59549c2b5797" />

- service – contine logica pentru produse si review-uri (ProductService, ReviewService), si trateaza exceptiile legate de acestea (de exemplu daca facem request pentru un produs ce nu exista in baza de date vom primi eroarea "Product not found with ID")
<img width="1872" height="1560" alt="image" src="https://github.com/user-attachments/assets/ff2ac0e9-fa18-4c46-a113-408f6dc29dca" />

- repository – asigura interactiunea cu baza de date cu ajutorul JpaRepository a produselor si review-urilor

- model – inregistreaza entitatile Product si Review si baza de date, alaturi de "caracteristicile" care se pot inregista pentru fiecare entitate (de exemplu un produs este descris prin ID de tip Long, nume de tip String, descriere de tip String, pret de tip Double si categorie de tip String, iar pentru Review se pot vedea caracteristicile in poza de mai jos)
<img width="1872" height="1556" alt="image" src="https://github.com/user-attachments/assets/2972eb99-7bbf-40cc-a585-358dd37383c3" />


- dto – obiecte pentru transferul datelor intre straturi, este folosit pentru diferentiera datelor introduse de utilizator, de datele ce se salveaza in baza de date. 
<img width="1589" height="1012" alt="image" src="https://github.com/user-attachments/assets/8a179587-34b2-46dc-8741-6ba45bdeeed6" />


- mapper – maparea dintre entitati si DTO-uri
<img width="2196" height="1168" alt="image" src="https://github.com/user-attachments/assets/90c4d72c-fe1c-4d59-ba53-d2c7a98b8dd4" />


- exception – 2 fisiere ce trateaza erori: 
GlobalExceptionHandler - trateaza erori de tip BAD_RQUEST, UNAUTHORIZED, NOT_FOUND si INTERNAL_SERVER_ERROR 
<img width="2734" height="1388" alt="image" src="https://github.com/user-attachments/assets/17a9844e-2c53-4809-af73-232f3fa7417c" />
ProductNotFoundException - trateaza eroarea unui produs ce nu exista in baza de date
<img width="1880" height="274" alt="image" src="https://github.com/user-attachments/assets/388c8234-33ba-483b-a0b4-44f6eda93061" />


### User Service

- controller – API-uri pentru login si register
<img width="2720" height="1342" alt="image" src="https://github.com/user-attachments/assets/cd5e65ab-932b-4691-b1c5-1fd7f4595243" />


- service – logica de autentificare prin token, inregistrare si validare
<img width="2720" height="1496" alt="image" src="https://github.com/user-attachments/assets/78feec33-9a48-466b-8066-48651e118d94" />


- repository – comunicarea cu baza de date pentru utilizatori - contine fisierul UserRepository
<img width="1834" height="410" alt="image" src="https://github.com/user-attachments/assets/992c3260-6e67-45bc-8289-2d68af5976c3" />


- model – entitatea User alaturi de "caracteristicile" pe care aceasta le are: ID de tip Long, username de tip String, password de tip String, email de tip String, role de tip String
<img width="1834" height="892" alt="image" src="https://github.com/user-attachments/assets/95773d78-853c-4f88-b76a-40d0d25f8686" />


- dto – request-uri pentru autentificare si inregistrare

- mapper – transforma obiecte intre entitati si DTO-uri
<img width="1834" height="716" alt="image" src="https://github.com/user-attachments/assets/8e496a21-9803-483c-a895-e8be1bb3c7ef" />


- config - contine 2 fisiere: JwtUtil ce genreaza un toket pentru fiecare cont de utilizator creat (in acest mod se genereaza o sesiune de utilizator autentificat)
<img width="1834" height="822" alt="image" src="https://github.com/user-attachments/assets/46f78453-2134-4c91-8672-9bd3ddf0dc08" />
SecurityConfig - salveaza parolele harshed in baza de date, dezactiveaza protectia CSRF (Cross-Site Request Forgery) pentru a nu interfera request-urile in Swagger cu token-urile CSRF si defineste regulile de acces (la path-urile de autentificare si inregistrare are acces toata lumea, pe cand orice alt request catre aplicatie necesita autentificare)
<img width="1834" height="896" alt="image" src="https://github.com/user-attachments/assets/e53da798-54a9-4130-b959-96bb22e00769" />


