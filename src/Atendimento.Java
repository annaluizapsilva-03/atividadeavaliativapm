import java.time.LocalDate; import java.time.LocalTime; import java.time.format.DateTimeFormatter;

public class Atendimento { private int codigo; private String nomeAnimal; private String especie; private String nomeTutor; private LocalDate data; private LocalTime horario; private StatusAtendimento status; private String observacoes; private Procedimento procedimento; private Sala sala; 

public Atendimento(int codigo, 
    String nomeAnimal, 
    String especie,
        String nomeTutor,
                    LocalDate data, 
                    LocalTime horario, 
                    String observacoes,
                    Procedimento procedimento) {
    this.codigo = codigo;
    this.nomeAnimal = nomeAnimal;
    this.especie = especie;
    this.nomeTutor = nomeTutor;
    this.data = data;
    this.horario = horario;
    this.observacoes = observacoes;
    this.procedimento = procedimento;
    this.status = StatusAtendimento.AGENDADO;
    this.sala = null;
}

public int getCodigo() { return codigo; }
public String getNomeAnimal() { return nomeAnimal; }
public StatusAtendimento getStatus() { return status; }
public Procedimento getProcedimento() { return procedimento; }
public Sala getSala() { return sala; }


public void atribuirSala(Sala sala) {
    this.sala = sala;
    this.status = StatusAtendimento.EM_ANDAMENTO;
}


public boolean finalizar() {
    if (sala == null) {
        return false;
    }
    this.status = StatusAtendimento.FINALIZADO;
    return true;
}

public String resumo() {
    return "Código: " + codigo + " | Animal: " + nomeAnimal + " (" + especie + ")"
            + " | Status: " + status.getDescricao();
}

public String detalhes() {
    DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    DateTimeFormatter hf = DateTimeFormatter.ofPattern("HH:mm");

    StringBuilder sb = new StringBuilder();
    sb.append("Código: ").append(codigo).append("\n");
    sb.append("Animal: ").append(nomeAnimal).append("\n");
    sb.append("Espécie: ").append(especie).append("\n");
    sb.append("Tutor: ").append(nomeTutor).append("\n");
    sb.append("Data: ").append(data.format(df)).append("\n");
    sb.append("Horário: ").append(horario.format(hf)).append("\n");
    sb.append("Status: ").append(status.getDescricao()).append("\n");
    sb.append("Observações: ").append(observacoes).append("\n");
    sb.append("Procedimento: ").append(procedimento).append("\n");

    if (sala != null) {
        sb.append("Sala: ").append(sala.getNumero()).append(" (Bloco ")
            .append(sala.getBloco()).append(", ").append(sala.getTipo()).append(")\n");
        if (sala.getVeterinario() != null) {
            sb.append("Veterinário: ").append(sala.getVeterinario().getNome()).append("\n");
        } else {
            sb.append("Veterinário: sala sem veterinário responsável\n");
        }
    } else {
        sb.append("Sala: não atribuída\n");
        sb.append("Veterinário: não definido\n");
    }
    return sb.toString();
}
}