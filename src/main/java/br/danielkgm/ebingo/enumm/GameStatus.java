package br.danielkgm.ebingo.enumm;

public enum GameStatus {
    NAO_INICIADO("Não Iniciado"),
    INICIADO("Iniciado"),
    ENCERRADO("Encerrado"),
    CANCELADO("Cancelado");

    private String value;

    GameStatus(String status) {
        this.value = status;
    }

    public String getStatus() {
        return this.value;
    }
}
