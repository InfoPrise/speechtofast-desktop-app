package com.speechtofast.speechtofast_desktop_app.dto;

import java.util.Objects;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class QueueItemDTO {
	private StringProperty diretorio;
	private StringProperty nomeArquivo;
	private StringProperty dataInsercao;
	private StringProperty progresso;
	private StringProperty status;
	private StringProperty dataFinalizacao;

	public QueueItemDTO(String diretorio, String nomeArquivo, String dataInsercao, String progresso, String status,
			String dataFinalizacao) {
		this.diretorio = new SimpleStringProperty(diretorio);
		this.nomeArquivo = new SimpleStringProperty(nomeArquivo);
		this.dataInsercao = new SimpleStringProperty(dataInsercao);
		this.progresso = new SimpleStringProperty(progresso);
		this.status = new SimpleStringProperty(status);
		this.dataFinalizacao = new SimpleStringProperty(dataFinalizacao);
	}

	public StringProperty getDiretorio() {
		return diretorio;
	}

	public void setDiretorio(StringProperty diretorio) {
		this.diretorio = diretorio;
	}

	public StringProperty getNomeArquivo() {
		return nomeArquivo;
	}

	public void setNomeArquivo(StringProperty nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}

	public StringProperty getDataInsercao() {
		return dataInsercao;
	}

	public void setDataInsercao(StringProperty dataInsercao) {
		this.dataInsercao = dataInsercao;
	}

	public StringProperty getProgresso() {
		return progresso;
	}

	public void setProgresso(StringProperty progresso) {
		this.progresso = progresso;
	}

	public StringProperty getStatus() {
		return status;
	}

	public void setStatus(StringProperty status) {
		this.status = status;
	}

	public StringProperty getDataFinalizacao() {
		return dataFinalizacao;
	}

	public void setDataFinalizacao(StringProperty dataFinalizacao) {
		this.dataFinalizacao = dataFinalizacao;
	}

	@Override
	public int hashCode() {
		return Objects.hash(dataFinalizacao, dataInsercao, diretorio, nomeArquivo, progresso, status);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		QueueItemDTO other = (QueueItemDTO) obj;
		return Objects.equals(dataFinalizacao, other.dataFinalizacao)
				&& Objects.equals(dataInsercao, other.dataInsercao) && Objects.equals(diretorio, other.diretorio)
				&& Objects.equals(nomeArquivo, other.nomeArquivo) && Objects.equals(progresso, other.progresso)
				&& Objects.equals(status, other.status);
	}

	@Override
	public String toString() {
		return "QueueItemDTO [diretorio=" + diretorio + ", nomeArquivo=" + nomeArquivo + ", dataInsercao="
				+ dataInsercao + ", progresso=" + progresso + ", status=" + status + ", dataFinalizacao="
				+ dataFinalizacao + "]";
	}

}
