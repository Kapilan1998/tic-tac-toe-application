let stompClient = null;
let game = null;
let player = null;

//Sends a message to the server using the STOMP client.
const sendMessage = (message) => {
    stompClient.send(`/app/${message.type}`, {}, JSON.stringify(message));
}


const makeMove = (move) => {
    sendMessage({
        type: "game.move",
        move: move,
        turn: game.turn,
        sender: player,
        gameId: game.gameId
    });
}

//object containing functions to handle each type of message received from the server.
const messagesTypes = {
    "game.join": (message) => {
        updateGame(message);
    },
    "game.gameOver": (message) => {
        updateGame(message);
        if (message.gameState === 'TIE') toastr.success(`Game over! It's a tie!`);
        else showWinner(message.winner);
    },
    "game.joined": (message) => {
        if (game !== null && game.gameId !== message.gameId) return;
        player = localStorage.getItem("playerName");
        updateGame(message);

        const socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, function (frame) {
            stompClient.subscribe(`/topic/game.${message.gameId}`, function (message) {
                handleMessage(JSON.parse(message.body));
            });
        });
    },
    "game.move": (message) => {
        updateGame(message);
    },
    "game.left": (message) => {
        updateGame(message);
        if (message.winner) showWinner(message.winner);
    },
    "error": (message) => {
        toastr.error(message.content);
    }
}


const handleMessage = (message) => {
    if (messagesTypes[message.type])
        messagesTypes[message.type](message);
}


const messageToGame = (message) => {
    return {
        gameId: message.gameId,
        board: message.board,
        turn: message.turn,
        player1: message.player1,
        player2: message.player2,
        gameState: message.gameState,
        winner: message.winner
    }
}

//Displays  name of the winner

const showWinner = (winner) => {
    toastr.success(`The winner is ${winner}!`);
    const winningPositions = getWinnerPositions(game.board);
    if (winningPositions && winningPositions.length === 3) {
        winningPositions.forEach(position => {
            const row = Math.floor(position / 3);
            const cell = position % 3;
            let cellElement = document.querySelector(`.row-${row} .cell-${cell} span`);
            cellElement.style.backgroundColor = '#b3e6ff';
        });
    }
}

//game starting. Asks the player to enter their name and sends a message to the server requesting to join the game.

const joinGame = () => {
    const playerName = prompt("Enter your name:");
    localStorage.setItem("playerName", playerName);
    sendMessage({
        type: "game.join",
        player: playerName
    });
}

//Connects the STOMP client to the server and subscribes to the "/topic/game.state" topic.
const connect = () => {
    const socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        stompClient.subscribe('/topic/game.state', function (message) {
            handleMessage(JSON.parse(message.body));
        });
        loadGame();
    });
}


const loadGame = () => {
    const playerName = localStorage.getItem("playerName");
    if (playerName) {
        sendMessage({
            type: "game.join",
            player: playerName
        });
    } else {
        joinGame();
    }
}


const updateGame = (message) => {
    game = messageToGame(message);
    updateBoard(message.board);
    document.getElementById("player1").innerHTML = game.player1;
    document.getElementById("player2").innerHTML = game.player2 || (game.winner ? '-' : 'Waiting for player 2...');
    document.getElementById("turn").innerHTML = game.turn;
    document.getElementById("winner").innerHTML = game.winner || '-';
}


const updateBoard = (board) => {
    let counter = 0;
    board.forEach((row, rowIndex) => {
        row.forEach((cell, cellIndex) => {
            const cellElement = document.querySelector(`.row-${rowIndex} .cell-${cellIndex}`);
            cellElement.innerHTML = cell === ' ' ? '<button onclick="makeMove(' + counter + ')"> </button>' : `<span class="cell-item">${cell}</span>`;
            counter++;
        });
    });
}

//Get the winner positions from the board.
const getWinnerPositions = (board) => {
    const winnerPositions = [];

    for (let i = 0; i < 3; i++) {
        if (board[i][0] === board[i][1] && board[i][1] === board[i][2] && board[i][0] !== ' ') {
            winnerPositions.push(i * 3);
            winnerPositions.push(i * 3 + 1);
            winnerPositions.push(i * 3 + 2);
        }
    }

    for (let i = 0; i < 3; i++) {
        if (board[0][i] === board[1][i] && board[1][i] === board[2][i] && board[0][i] !== ' ') {
            winnerPositions.push(i);
            winnerPositions.push(i + 3);
            winnerPositions.push(i + 6);
        }
    }

    if (board[0][0] === board[1][1] && board[1][1] === board[2][2] && board[0][0] !== ' ') {
        winnerPositions.push(0);
        winnerPositions.push(4);
        winnerPositions.push(8);
    }

    return winnerPositions;
}

window.onload = function () {
    connect();
}
