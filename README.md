<h1 align="center">
    <img src="https://i.imgur.com/NzIDvNA.png" width="500"/>
</h1>

<p align="center">
<sup>
Gereco is a competitive events manager written in Java using JavaFX
</sup>
</p>

![GitHub license](https://img.shields.io/github/license/pedrogneri/gereco.svg)
![Github commits](https://img.shields.io/github/commit-activity/m/pedrogneri/gereco.svg)
![GitHub forks](https://img.shields.io/github/forks/pedrogneri/gereco?style=social)

## Features 
- Login / register
- Event CRUD
- Institution configs
- Team grid
- Group teams with draw
- Generate first stage matches
- Generate final matches
- Generate Leader board
- Generate Leader board as PDF

## Dependencies
- [JFoeniX](https://github.com/jfoenixadmin/JFoenix)
- [itextpdf](https://itextpdf.com/en)
- [Gson](https://github.com/google/gson)
- [Commons codec](https://commons.apache.org/proper/commons-codec/)
- [Mongodb java driver](https://mongodb.github.io/mongo-java-driver/)

## Contribution
Check out the issues to find out how you can contribute to development,
or if you want, create your own issue and submit a PR.

To contribute, you will need:
- Gradle 
- JFoenix
- Java 1.8
- SceneBuilder (or you can edit FXML directly on Intellij)

Gereco uses a free service to host the database, so if you want to test faster, 
just remove the connection string in 
[MongoConnection.java](https://github.com/pedrogneri/gereco/blob/master/src/main/java/services/MongoConnection.java), 
but you will need to have mongoDB installed.

## Members
Gereco is a computer course term paper of ETEC João Belarmino. And these are the members who developed 
this software:

| <img src="https://avatars.githubusercontent.com/luizgustavo234" width=115> | <img src="https://avatars.githubusercontent.com/MatheusGualtieri" width=115> | <img src="https://avatars.githubusercontent.com/pedrogneri" width=115> | <img src="https://avatars.githubusercontent.com/ryteck" width=115> | <img src="https://avatars.githubusercontent.com/ThomasRibeiro" width=115> |  
|---|---|---|---|---|
| <a href="https://github.com/luizgustavo234">@luizgustavo234</a> | <a href="https://github.com/MatheusGualtieri">@MatheusGualtieri</a> | <a href="https://github.com/pedrogneri">@pedrogneri</a> | <a href="https://github.com/ryteck">@ryteck</a> | <a href="https://github.com/ThomasRibeiro">@ThomasRibeiro</a> |

## License
Gereco is released under the MIT license. [Click here](https://github.com/pedrogneri/gereco/blob/master/LICENSE) 
for details.
