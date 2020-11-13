# PoneyClub

## Application URL
If started on local machine go to : http://localhost:4200/

## Application Description
PoneyClub is a webapp for a riding stable management. 
Many feature are available like user management, horse management and course management.
If you are a Rider you could use it by signing up.
If you are a Monitor you could use it only if an admin create your account.
If you are an admin you could use it only if a super-admin create your account.

## Application account available by default (for testing) :

 ______________________________________________
|ROLE          |   USERNAME        |   PASSWORD|
|______________________________________________| 
|Super-admin   | root@gmail.com    | changeit  |  
|Admin         | admin@gmail.com   | changeit  |
|Monitor       | teacher@gmail.com | changeit  |
|Rider         | rider@gmail.com   | changeit  |
|______________________________________________|

## *Authors*
```
Amine MAZA
Rémi TRUONG
```
## Installation

clone the repository on your machine.

## Front

```
cd {Your_local_path}/Front/PoneyClub
npm install
```

## Back
```
cd {Your_local_path}/Back/PoneyClub
mvn clean install
```


## Run

Front :
```
ng serve
```

Back :
```
mvn spring-boot:run
```
