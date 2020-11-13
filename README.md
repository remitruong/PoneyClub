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

<table>
  <thead>
   <tr>
    <th>ROLE</th>
    <th>USERNAME</th>
    <th>PASSWORD</th>
   </tr>
  </thead>
  <tbody>
   <tr>
    <td>Super-admin</td>
    <td>root@gmail.com </td>
    <td>changeit</td>
   </tr>
   <tr>
    <td>Admin</td>
    <td>admin@gmail.com</td>
    <td>changeit</td>
   </tr>
   <tr>
    <td>Monitor</td>
    <td>teacher@gmail.com</td>
    <td>changeit</td>
   </tr>
   <tr>
    <td>Rider</td>
    <td>rider@gmail.com</td>
    <td>changeit</td>
   </tr>
 </tbody>
 </table>

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
