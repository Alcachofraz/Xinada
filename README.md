# Xinada

My plugin is free for everyone to use, but feel free to donate, if you feel like it ;)  
  
[![Donate](https://img.shields.io/badge/Donate-PayPal-green.svg)](https://www.paypal.com/donate?hosted_button_id=LR9UAGCU57HWG)

---------

## Demonstration Video (Portuguese)

This is an old video of an old version of the plugin, and there were some bugs. There still are, surely. I just haven't found them yet :)  
This video serves a purpose of simply displaying the plugin's functionality. [Here](https://www.youtube.com/watch?v=jDewPIYbnOs&list=PLqYULEfN6pGFgyhM-wHbFiRz8Ev3cCu2b&index=1&ab_channel=PedroGon%C3%A7alves) it is.

---------

## Idioms

[English](#shortcuts)  
  
[Português](#atalhos)  

---------

## <a name="shortcuts"></a>Shortcuts

1. [Introduction](#introduction)  
2. [Setup](#setup)  
3. [Configuration](#configuration)  
4. [Commands](#commands)  
5. [Roles](#roles)  

## <a name="introduction"></a>1. Introduction

The Xinada Project consists of a plugin for the **Minecraft** game. The plugin organizes and starts rounds of a mini-game based on the well-known **Murder Mystery**.
For those unfamiliar with the **Murder Mystery** game, here's a brief explanation: At least 3 players are required and there are 3 different roles. The **Police**, the **Assassin** and the **Innocent**. The **Assassin** must wipe all players out, while the **Police** must identify and arrest the Assassin before he achieves his goal. The **Innocent** will be just watching and trying to survive. In case there are 5 players, for example, 3 of them would be **Innocent**.

The **Murder Mystery** game is already quite fun in itself. But what if all **Innocents** had a unique ability? What if in addition to **Innocents**, there were other roles that may help or frame the **Assassin**? That's where the Xinada plugin comes in! With over 30 different aroles, Xinada offers a mini-game with great replayability! At least 3 players are required, the more the better (maximum 10).


## <a name="setup"></a>2. Setup

To properly setup the plugin follow these steps:

1. Make sure you're using the **1.17** version of Minecraft;  

2. Put **xinada-X.X.X.jar** in your Spigot server plugins folder (**.../server_folder/plugins/**);
<img src="https://user-images.githubusercontent.com/75852333/136671649-419dca9b-2b24-49ee-a497-df9386cfcf0b.png" width="720px">

3. Run the server once. When the server loads up, you should see somewhere in the console: **"[Xinada] Enabling Xinada vX.X"**, **X.X** being the version of the plugin;
<img src="https://user-images.githubusercontent.com/75852333/136671838-12ff3eb0-1f75-4122-acda-58f50901de54.pn" width="720px">

4. Close the server;  

5. Back in your server folder, you'll see the plugin created the world **Xinada**, as well as **Xinada_nether** and **Xinada_the_end**. This happened because the default world for this plugin is called **Xinada**. This can be changed in the plugin's config files (more on that [here](#configuration));
<img src="https://user-images.githubusercontent.com/75852333/136671877-cf2f3ee0-ea89-4edb-9b50-d2d8f5843ffa.png" width="720px">


6. Also, inside the plugins folder, you'll see a new folder called **Xinada**. This folder has important config files inside. They all have flags you can mess with, except for the stringsXX.yml files. You don't want to mess with those, since they contain translations of Xinada's supported idioms. Click [here](#configuration) to learn more about Xinada **Configuration Files**.
<img src="https://user-images.githubusercontent.com/75852333/136671909-1c4a721e-9012-4d41-a3f3-d85807e074df.png" width="720px">

7. Now that you know how to use the **Configuration Files**, all that's left is add maps to **maps.yml**. All you have to do is create a world containing various different small maps (this would be easier with [mcedit](https://www.mcedit.net/)). You can find lots of small maps on [planetminecraft.com](https://www.planetminecraft.com/), if you search for something in the lines of **"Murder Mystery map"** or **"TTT map"** or **"Call of Duty map"**. You can add whatever maps you like. Just make sure players won't be able to walk out of them in-game. You can use [this](https://www.dropbox.com/s/ttzofengrb4glcs/Xinada.zip?dl=1) dropbox link to download the world compatible with the default **maps.yml** the plugin generated.  

8. Then, you'll have to choose 10 spawn points for each small map and fill **maps.yml** with that information. You can see how [here](#configuration). You don't need to do this if you are using the world provided by the last step's dropbox link.  

9. That's it! Now use the [commands](#commands) to start playing! Enjoy!  

10. For a better experience, use the [NoNameTag](https://www.spigotmc.org/resources/no-name-tag.70128/) plugin to prevent players from seeing name tags through walls.

If you run into any issues, feel free to contact me.


## <a name="configuration"></a>3. Configuration

#### 3.1. Game Configuration

In the **game.yml** file, you may adjust some game related flags.

#### 3.2. World Configuration

These are the world configurations. In the **world.yml** file is where you may change the name of the default world Xinada will use. You can also adjust lobby coordinates. These are the coordinates to which the players will be teleported when they join and when a Xinada game ends. It's the lobby in its essence.

#### 3.3. Maps Configuration

The **maps.yml** file is where you setup and add your maps. All maps must be contained inside the same minecraft world (this easier to achieve using [mcedit](https://www.mcedit.net/)). The template in **maps.yml** is really easy to follow. Just add the name of the map (whatever you like) and 10 different spawn points where players may spawn in the beggining of a Xinada round. Example of a map:

~~~~~~~~~~~~~~~~~~~~~  
whatever_name_i_like:  
  location1:  
    x: 287.5  
    y: 56  
    z: 857.5  
  ...  
  location10:  
    x: 331.5  
    y: 56  
    z: 847.5  
~~~~~~~~~~~~~~~~~~~~~  

#### 3.4. Strings Configuration

Unless you don't want to play the plugin in english or portuguese, you won't need to mess with the **stringsXX.yml** files. If you are, let's say, italian and want to play Xinada in italian, you'll need to generate your own strings. Just create a **stringsIT.yml** file with the same content as the others **stringsXX.yml** files, but in italian. Then, go to **game.yml** and change **language: "EN"** to **language: "IT"**.

## <a name="commands"></a>4. Commands

1. Use **/start** to start a game with the players that are in the server. A game starts in 5 seconds.  
2. Use **/end** to end the current game.  
3. Use **/next** to jump to the next round. No points are assigned to any players.  
4. Use **/role** to see your role and objective, if you forgot.  


## <a name="roles"></a>5. Roles

| Paper | Side | Points | Description
|:-------------:|:------------:|:-----|:-------
| Cop | Good | Survive (2P) | Identify the Assassin and kill him!
| Assassin | Bad | Survive (2P) | Kill everything and everyone!
| Innocent | Good | Survive (2P) | That's it.
| Doctor | Good | Survive (1P) & Heal a Player (1P) | Right click to revive a dead player!
| Ghost | Good | Survive (2P) | When you die, you can throw snowballs to revive another player!
| Guru | Good | Survive (1P) & Expose the Assassin (1P) | Left click on an alive player. If you hit the Assassin, he raises the knife!
| Witch | Good | Survive (2P) | Left click on a player to curse him. No matter what he does, he will earn 0 points!
| Priest | Good | Survive (1P) & Bless a Player (1P) | Left click on a player to bless him. No matter what he does, he will earn 3 points!
| Hunter | Good | Survive (2P) | Set up to 3 traps to slow down other players!
| Illusionist | Good | Survive (2P) | Right click on the floor to use a smoke bomb. All players around you will be blinded and slowed down!
| Survivor | Good | Survive (2P) | When you die, you get a second chance!
| Promoter | Good | Survive (2P) | Left click on an alive player to promote him to a Cop!
| Negotiator | Good | Survive (1P) & Heal Each Other (1P) | Left click on a player to become partners. From there on, you can revive each other!
| Ninja | Good | Survive (2P) | You have an invisibility potion that lasts a few seconds!
| Pirate | Good | Survive (1P) & Find the other Pirate's Treasure (1P) | Hide your treasure, and find the other Pirate's one!
| Analyst | Good | Survive (2P) | Left click on a dead player to find out who killed him!
| Engineer | Good | Survive (2P) | Use the compass to spot the closest Assassin!
| Electrician | Good | Survive (2P) | Use the charcoal to switch off the lights.
| Psychic | Good | Survive (2P) | Left click on the eye to save your location (you can do this as often as you like). Right click on the eye to teleport to the saved location (you can only do this 1 time)!
| Immune | Good | Survive (2P) | You are immune to the other players' abilities!
| Fisherman | Good | Survive (2P) | Use the fishing rod to throw other players away!
| Magician | Good | Survive (2P) | Use the flower to turn the Assassin's knife into a harmless tulip for a few seconds!
| Clown | Good | Survive (2P) | Use the paper to tell a joke and distract the Assassins!
| Athlete | Good | Survive (2P) | You have a speed potion that lasts a few seconds!
| Sheep | Good | Survive (1P) & Flee the Shepherd (1P) | The Shepherd will try to cut your wool. Stay away from him.
| Shepherd | Good | Survive (1P) & Cut the Sheep Wool (1P) | Find your lost sheep and cut its wool.
| Jesus | Good | Survive (2P) | Light a candle and all ba guys have to type "amen" to be able to move again.
| Traitor | Bad | Survive (2P) | If the Assassin kills you, you become a second Assassin!
| Accomplice | Bad | Survive (2P) | The Assassin knows who you are and can lend you the knife!
| Devil | Bad | Survive (1P) & Fused Souls Die (1P) | Left click on two different alive players to merge their souls. When one of them dies, the other dies as well!
| Monster | Bad | Demote a Police (2P) | You are immortal. Your mission is to demote the Cop!
| Cupid | Bad | Survive (1P) & Passionate Players Die (1P) | Left click on two different alive players so they fall in love. From there on, they can't get away from each other, or they die!
| Flutist | Bad | Survive (2P) | Use the stick to select the players you want. Right click to teleport them to you!
| Cheater | Bad | Survive (2P) | Use the skull to inform other players of your (fake) death!
| Terrorist | Bad | Explode the bomb (2P) | Survive (1P) | Use the bomb to explode everything and everyone. Activate all fuses before another player stops you!
| Pyrotechnic | Bad | Survive (2P) | Use the rocket to expose the Cop's location.
| Thief | Neutral | Score accordingly | Left click on an alive player to steal his ability!
| Gravedigger | Neutral | Score accordingly | Left click on a dead player to steal his ability!

----------

## <a name="atalhos"></a>Atalhos

1. [Introdução](#introdução)  
2. [Preparação](#preparação)  
3. [Configuração](#configuração)  
4. [Comandos](#comandos)  
5. [Papéis](#papéis)  

## <a name="introdução"></a>1. Introdução

O Projeto Xinada consiste num plugin para o jogo **Minecraft**. O plugin organiza e inicia rondas de um mini-jogo baseado no conhecido **Murder Mystery**.
Para quem não conhece o jogo **Murder Mystery**, eis uma breve explicação: São necessários pelo menos 3 jogadores e existem 3 papéis diferentes. O papel de **Polícia**, o papel de **Assassino** e o papel de **Inocente**. O **Assassino** tem de limpar o sebo a todos os jogadores, e o **Polícia** tem de identificar e prender o Assassino antes que este atinja o seu objetivo. O **Inocente** fica apenas a observar e a tentar sobreviver. No caso de haverem 5 jogadores, por exemplo, 3 deles seriam **Inocentes**.

O jogo **Murder Mystery** é já bastante divertido por si mesmo. Mas e se todos os **Inocentes** tivessem uma habilidade única? E se além de **Inocentes**, existissem outros papéis cujo objetivo consiste em ajudar o **Assassino**? É aí que entra o plugin Xinada! Com mais de 30 habilidades diferentes, o Xinada oferece um mini-jogo com grande rejogabilidade! São necessários pelo menos 3 jogadores, sendo que quantos mais, melhor (máximo de 10).


## <a name="preparação"></a>2. Preparação

Para preparar o plugin corretamente, segue estas etapas:

1. Certifica-te que estás a usar a versão **1.17** do Minecraft;  

2. Coloca **xinada-X.X.X.jar** na pasta de plugins do teu servidor Spigot (**.../server_folder/plugins/**);
<img src="https://user-images.githubusercontent.com/75852333/136671649-419dca9b-2b24-49ee-a497-df9386cfcf0b.png" width="720px">

3. Executa o servidor uma vez. Quando o servidor carregar, deverás conseguir ver algures na consola: **"[Xinada] Ativando Xinada vX.X"**, sendo **X.X** a versão do plugin;
<img src="https://user-images.githubusercontent.com/75852333/136671838-12ff3eb0-1f75-4122-acda-58f50901de54.pn" width="720px">

4. Termina o servidor;  

5. De volta à pasta do servidor, verás que o plugin criou o mundo **Xinada**, bem como **Xinada_nether** e **Xinada_the_end**. Isso aconteceu porque o mundo padrão para este plugin chama-se **Xinada**. Isso pode ser alterado nos ficheiros de configuração do plugin (mais sobre isso [aqui](#configuração));
<img src="https://user-images.githubusercontent.com/75852333/136671877-cf2f3ee0-ea89-4edb-9b50-d2d8f5843ffa.png" width="720px">

6. Além disso, dentro da pasta plugins, você verá uma nova pasta chamada **Xinada**. Esta pasta contém ficheiros de configuração importantes. Todos eles têm campos que podem ser alterados consoante as preferências de cada um, exceto os ficheiros stringsXX.yml. Não deves mexer nesses porque contêm as traduções para os idiomas suportados pelo Xinada. Clica [aqui](#configuração) para saberes mais sobre os **ficheiross de configuração** do Xinada.
<img src="https://user-images.githubusercontent.com/75852333/136671909-1c4a721e-9012-4d41-a3f3-d85807e074df.png" width="720px">

7. Agora que sabes usar os **ficheiros de configuração**, tudo o que resta é adicionar mapas a **maps.yml**. Tudo que você precisa fazer é criar um mundo (mundo minecraft) contendo vários pequenos mapas (o software [mcedit](https://www.mcedit.net/) facilita imenso este passo). Poderás encontrar vários mapas adequados em [planetminecraft.com](https://www.planetminecraft.com/), se pesquisares algo nas linhas de **"Murder Mystery map"** ou **"mapa TTT"** ou **"Mapa do Call of Duty"**. Podes adicionar os mapas que bem te apetecerem. Apenas certifica-te de que os jogadores não conseguem sair deles durante o jogo. Podes usar [este](https://www.dropbox.com/s/ttzofengrb4glcs/Xinada.zip?dl=1) link da Dropbox para fazer download de um mundo minecraft compatível com o ficheiro **maps.yml** gerado pelo plugin.  


8. Por fim, terás de escolher 10 pontos de spawn para cada mapa e preencher **maps.yml** com essa informação. Podes ver como fazê-lo [aqui](#configuração).

9. É isso mesmo! Agora usa os [comandos](#comandos) para começar a jogar! Diverte-te!  

10. Para uma melhor experiência, utiliza o plugin [NoNameTag](https://www.spigotmc.org/resources/no-name-tag.70128/) para que não se consiga ver nomes de outros jogadores através das paredes.  

Se surgir algum problema, sinta-se à vontade para entrar em contato comigo.


## <a name="configuração"></a>3. Configuração

#### 3.1. Configuração do jogo

No ficheiro **game.yml**, poderás ajustar alguns campos relacionados com o jogo.

#### 3.2. Configuração do mundo minecraft

Estas são as configurações do mundo minecraft. O ficheiro **world.yml** é onde poderás alterar o nome do mundo padrão que o Xinada utilizará. Também podes ajustar as coordenadas do lobby. Estas são as coordenadas para as quais os jogadores serão teletransportados quando se juntarem e quando um jogo do Xinada terminar. É o lobby na sua essência.

#### 3.3. Configuração de mapas

O ficheiro **maps.yml** é onde irás configurar e adicionar os teus mapas. Todos os mapas devem estar contidos no mesmo mundo minecraft (mais fácil com a ajuda do software [mcedit] (https://www.mcedit.net/)). O exemplo em **maps.yml** é muito fácil de seguir. Basta adicionar o nome do mapa (o que quiseres) e 10 pontos de spawn diferentes onde os jogadores poderão nascer no início de uma rodada do Xinada. Exemplo de mapa:

~~~~~~~~~~~~~~~~~~~~~  
o_nome_que_me_apetecer:  
  location1:  
    x: 287.5  
    y: 56  
    z: 857.5  
  ...  
  location10:  
    x: 331.5  
    y: 56  
    z: 847.5  
~~~~~~~~~~~~~~~~~~~~~  

#### 3.4. Configuração das strings

A menos que não queiras jogar o plugin em inglês ou português, não precisarás mexer nos arquivos **stringsXX.yml**. Se fores, digamos, italiano e quiseres jogar ao Xinada em italiano, precisarás de gerar as tuas próprias strings. Basta criar um arquivo **stringsIT.yml** com o mesmo conteúdo dos outros arquivos **stringsXX.yml**, mas em italiano. Em seguida, vais ao ficheiro **game.yml** e alteras **language: "EN"** para **language: "IT"**.


## <a name="comandos"></a>4. Comandos

1. Utiliza **/start** para iniciar um jogo com os jogadores que estão no servidor. Um jogo começará em 5 segundos.
2. Utiliza **/end** para encerrar o jogo atual.
3. Utiliza **/next** para pular para a próxima ronda. Nenhum ponto é atribuído a qualquer jogador.
4. Utiliza **/role** para veres o teu papel e objetivo, caso te tenhas esquecido.  


## <a name="papéis"></a>5. Papéis

| Papel         | Lado          | Pontos | Descrição
|:-------------:|:-------------:|:-----|:-------
| Polícia | Bonzinho | Sobrevive (2P) | Identifica o Assassino e mata-o!
| Assassino | Mauzão | Sobrevive (2P) | Mata tudo e todos!		
| Inocente | Bonzinho | Sobrevive (2P) | É só isso.
| Médico | Bonzinho | Sobrevive (1P) & Cura um jogador (1P) | Clica com o lado direito para reviver um jogador morto!
| Fantasma | Bonzinho | Sobrevive (2P) | Quando morreres, podes atirar bolas de neve para reviver outro jogador!
| Guru | Bonzinho | Sobrevive (1P) & Expõe o Assassino (1P) | Clica com o lado esquerdo num jogador vivo. Se acertares no Assassino, ele levanta a faca!
| Bruxa | Bonzinho | Sobrevive (2P) | Clica com o lado esquerdo num jogador para o amaldiçoares. Independentemente do que fizer, ele ganhará 0 pontos!
| Padre | Bonzinho | Sobrevive (1P) & Abençoa um jogador (1P) | Clica com o lado esquerdo num jogador para o abençoares. Independentemente do que fizer, ele ganhará 3 pontos!
| Caçador | Bonzinho | Sobrevive (2P) | Põe até 3 armadilhas, para atrasares outros jogadores!
| Ilusionista | Bonzinho | Sobrevive (2P) | Clica com o lado direito no chão para usares uma bomba de fumo. Todos os jogadores à tua volta serão cegados e atrasados!
| Matulão | Bonzinho | Sobrevive (2P) | Quando morres, recebes uma segunda oportunidade!
| Promotor | Bonzinho | Sobrevive (2P) | Clica com o lado esquerdo num jogador vivo para o promoveres a Polícia!
| Negociador | Bonzinho | Sobrevive (1P) & Curam-se um ao outro (1P) | Clica com o lado esquerdo num jogador para se tornarem sócios. A partir daí, podem reviver-se um ao outro!
| Ninja	 | Bonzinho | Sobrevive (2P) | Tens uma poção de invisibilidade que dura alguns segundos!
| Pirata | Bonzinho | Sobrevive (1P) & Encontra o tesouro do outro Pirata (1P) | Esconde o teu tesouro, e encontra o do outro Pirata!
| Analista | Bonzinho | Sobrevive (2P) | Clica com o lado esquerdo num jogador morto para saberes quem o matou!
| Engenheiro | Bonzinho | Sobrevive (2P) | Usa a bússula para detetar o Assassino mais próximo!
| Eletricista | Bonzinho | Sobrevive (2P) | Usa o carvão para rebentar com o quadro da luz.
| Psíquico | Bonzinho | Sobrevive (2P) | Clica com o lado esquerdo no olho para salvares a tua localização (podes fazê-lo as vezes que quiseres). Clica com o lado direito no olho para te teletransportares para a localização salva (só podes fazê-lo 1 vez)!
| Imune | Bonzinho | Sobrevive (2P) | És imune às habilidades dos outros jogadores!
| Pescador | Bonzinho | Sobrevive (2P) | Usa a cana de pesca para afastares outros jogadores!
| Mágico | Bonzinho | Sobrevive (2P) | Usa a flor para transformares a faca do Assassino numa inofensiva rosa, durante alguns segundos!
| Palhaço | Bonzinho | Sobrevive (2P) | Usa o papel para contares um piada e desconcentrar o Assassino!
| Atleta | Bonzinho | Sobrevive (2P) | Tens uma poção de velocidade que dura alguns segundos!
| Ovelha | Bonzinho | Sobrevive (1P) & Foge ao Pastor (1P) | O pastor vai tentar cortar a tua lã. Mantem-te afastado dele.
| Pastor | Bonzinho | Sobrevive (1P) & Corta a lã à ovelha (1P) | Encontra a tua ovelha perdida e corta-lhe a lã.
| Jesus	| Bonzinho | Sobrevive (2P) | Acende uma vela e todos os mauzões têm de digitar "amen" para voltarem a conseguir mexer-se.				
| Traidor | Mauzão | Sobrevive (2P) | Se o Assassino te matar, transformas-te num segundo Assassino!
| Cúmplice | Mauzão | Sobrevive (2P) | O Assassino sabe quem tu és e pode emprestar-te a faca!
| Diabo	| Mauzão | Sobrevive (1P) & As almas fundidas morrem (1P) | Clica com o lado esquerdo em dois jogadores vivos diferentes para fundir as suas almas. Quando um deles morrer, o outro também morre!
| Monstro | Mauzão | Despromove um Polícia (2P) | És imortal. A tua missão é despromover Polícias!
| Cupido | Mauzão | Sobrevive (1P) & Jogadores apaixonados morrem (1P) | Clica com o lado esquerdo em dois jogadores vivos diferentes para os apaixonares. A partir daí, eles não se podem afastar um do outro, senão morrem!
| Flautista | Mauzão | Sobrevive (2P) | Usa o pau para selecionares os jogadores que quiseres. Clica com o lado direito para os teletransportares para ti!
| Batoteiro | Mauzão | Sobrevive (2P) | Usa a caveira para informar os outros jogadores da tua (falsa) morte!
| Terrorista | Mauzão | Explode com a bomba (2P) | Sobrevive (1P) | Usa a bomba para explodir com tudo e todos. Ativa todos os fusíveis antes que outro jogador te impeça!
| Pirotécnico | Mauzão | Sobrevive (2P) | Usa o foguete para expor a localização do polícia.			
| Ladrão | Neutro | Pontua conforme | Clica com o lado esquerdo num jogador vivo, para lhe roubares a habilidade!
| Coveiro | Neutro | Pontua conforme | Clica com o lado esquerdo num jogador morto, para lhe roubares a habilidade!
