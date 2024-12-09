package com.tictactoe.controller;

import com.tictactoe.controller.dto.ConnectRequest;
import com.tictactoe.exception.InvalidGameException;
import com.tictactoe.exception.InvalidParamException;
import com.tictactoe.exception.NotFoundException;
import com.tictactoe.model.Game;
import com.tictactoe.model.GamePlay;
import com.tictactoe.model.Player;
import com.tictactoe.service.GameService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/game")
public class GameController {

    private final GameService gameService;
    private final SimpMessagingTemplate simpMessagingTemplate;


    @PostMapping("/start")
    public ResponseEntity<Game> start(@RequestBody Player player) {
        log.info("start game request: {}", player);
// {
//   "login": "radha"
// }
//this way the player1 will set. 
//for the first time a gameId will be generated. and a player1 is set.
        return ResponseEntity.ok(gameService.createGame(player));
    }
    
    
    @PostMapping("/connect")
    public ResponseEntity<Game> connect(@RequestBody ConnectRequest request) throws InvalidParamException, InvalidGameException {
//   "player1": {
//     "login": "krishna"
//   },
//   "gameId":"29bb1fa8-6ea9-4018-8cd4-2fb3cb7088ea"
//
//	here we are sending the ConnectRequest as the player login and gameId from the postman     
    	log.info("connect request: {}", request);
        return ResponseEntity.ok(gameService.connectToGame(request.getPlayer(), request.getGameId()));
    }

    
    @PostMapping("/connect/random")
    public ResponseEntity<Game> connectRandom(@RequestBody Player player) throws NotFoundException {
//this is gonna set the player2's information in the game class    

    	log.info("connect random {}", player);
        return ResponseEntity.ok(gameService.connectToRandomGame(player));
    }


    
    
    
//form the JS this will draw the stomp connection in the (url + "/gameplay")   
/*function connectToSocket(gameId) {
//this is gonna create the stomp connection. 
    console.log("connecting to the game");
    let socket = new SockJS(url + "/gameplay");
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log("connected to the frame: " + frame);
        stompClient.subscribe("/topic/game-progress/" + gameId, function (response) {
            let data = JSON.parse(response.body);
            console.log(data);
            displayResponse(data);
        })
    })
}
*/    
    @PostMapping("/gameplay")
    public ResponseEntity<Game> gamePlay(@RequestBody GamePlay request) throws NotFoundException, InvalidGameException {
        log.info("gameplay: {}", request);
        System.out.println();
        Game game = gameService.gamePlay(request);
        //  /topic/game-progress is the broadcast media 
        //it sends the game object to the broadcast media. And from the broadcast media 
        //every stomp client will get the message
        simpMessagingTemplate.convertAndSend("/topic/game-progress/" + game.getGameId(), game);
        return ResponseEntity.ok(game);
    }
}