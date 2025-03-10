package br.danielkgm.ebingo.enumm;

public enum GameAction {
    CREATED("Jogo criado"),
    STARTED("Jogo iniciado"),
    ENDED_BY_ADM("Jogo encerrado"),
    EDITED("Jogo editado"),
    NUMBER_DRAWN("Número sorteado"),
    NUMBER_MARKED("Número marcado na cartela"),
    GAME_WON("Jogo vencido"),
    PRIZE_VIEWED("Prêmio visualizado"),
    PLAYER_JOINED("Entrou no jogo");

    private String action;

    GameAction(String action) {
        this.action = action;
    }

    public String getAction() {
        return this.action;
    }
}
