import java.time.LocalDate; 
import java.time.LocalTime; 
import java.time.format.DateTimeFormatter;
import java.util.ArrayList; 
import java.util.Scanner;

public class Main {

private static final Scanner sc = new Scanner(System.in);
private static final ArrayList<Veterinario> veterinarios = new ArrayList<>();
private static final ArrayList<Sala> salas = new ArrayList<>();
private static final ArrayList<Atendimento> atendimentos = new ArrayList<>();
private static int proximoCodigo = 1;

public static void main(String[] args) {
    carregarDadosIniciais();

    int opcao;
    do {
        exibirMenu();
        opcao = lerInt("Escolha uma opção: ");
        System.out.println();

        switch (opcao) {
            case 1: cadastrarAtendimento(); break;
            case 2: associarVeterinarioSala(); break;
            case 3: atribuirAtendimentoSala(); break;
            case 4: exibirAtendimentosDaSala(); break;
            case 5: totalFinalizadosPorSala(); break;
            case 6: buscarPorStatus(); break;
            case 7: exibirDetalhesAtendimento(); break;
            case 8: finalizarAtendimento(); break;
            case 0: System.out.println("Encerrando o sistema..."); break;
            default: System.out.println("Opção inválida!");
        }
    } while (opcao != 0);
}


private static void carregarDadosIniciais() {
    veterinarios.add(new Veterinario("Maria Luiza", "129.876.333-44", "Clínica Geral", "(31) 98866-0055"));
    veterinarios.add(new Veterinario("Davi Simão", "457.890.334-66", "Cirurgia", "(31) 99977-3546"));
    veterinarios.add(new Veterinario("Anna Luiza", "157.442.666-67", "Dermatologia", "(31) 91234-5678"));

    salas.add(new Sala(1, "A", 3, "Consulta"));
    salas.add(new Sala(2, "A", 2, "Cirurgia"));
    salas.add(new Sala(3, "B", 4, "Exames"));
}

private static void exibirMenu() {
    System.out.println("\n===== CLÍNICA VETERINÁRIA =====");
    System.out.println("1 - Cadastrar atendimento");
    System.out.println("2 - Associar veterinário a uma sala");
    System.out.println("3 - Atribuir atendimento a uma sala");
    System.out.println("4 - Exibir atendimentos de uma sala");
    System.out.println("5 - Total de atendimentos finalizados por sala");
    System.out.println("6 - Buscar atendimentos por status");
    System.out.println("7 - Exibir detalhes de um atendimento");
    System.out.println("8 - Finalizar atendimento");
    System.out.println("0 - Sair");
}


private static void cadastrarAtendimento() {
    System.out.println("--- Cadastrar atendimento ---");
    String nomeAnimal = lerTexto("Nome do animal: ");
    String especie = lerTexto("Espécie: ");
    String tutor = lerTexto("Nome do tutor: ");
    LocalDate data = lerData("Data (dd/MM/yyyy): ");
    LocalTime horario = lerHora("Horário (HH:mm): ");
    String obs = lerTexto("Observações: ");

    System.out.println("-- Procedimento --");
    String nomeProc = lerTexto("Nome do procedimento: ");
    int duracao = lerInt("Duração estimada (minutos): ");
    double valor = lerDouble("Valor (R$): ");
    System.out.println("Complexidade: 1 - Baixa | 2 - Média | 3 - Alta");
    int c = lerInt("Escolha: ");
    String complexidade = (c == 1) ? "Baixa" : (c == 3) ? "Alta" : "Média";

    Procedimento proc = new Procedimento(nomeProc, duracao, valor, complexidade);

    System.out.println("Status: 1 - Agendado | 2 - Em andamento | 3 - Finalizado");
    int st = lerInt("Escolha: ");

    Atendimento at = new Atendimento(proximoCodigo, nomeAnimal, especie, tutor, data, horario, obs, proc);

    
    if (st == 2 || st == 3) {
        System.out.println("Esse status exige uma sala. Escolha a sala:");
        Sala sala = escolherSala();
        if (sala == null) {
            System.out.println("Sala não encontrada. Atendimento salvo como AGENDADO.");
        } else {
            boolean verificarOcupacao = (st == 2);
            String erro = validarSala(sala, at, verificarOcupacao);
            if (erro != null) {
                System.out.println(erro + " Atendimento salvo como AGENDADO.");
            } else {
                at.atribuirSala(sala);
                if (st == 3) {
                    at.finalizar();
                }
            }
        }
    }

    atendimentos.add(at);
    proximoCodigo++;
    System.out.println("Atendimento cadastrado com sucesso! Código: " + at.getCodigo()
            + " | Status: " + at.getStatus().getDescricao());
}


private static void associarVeterinarioSala() {
    System.out.println("--- Associar veterinário a uma sala ---");
    for (int i = 0; i < veterinarios.size(); i++) {
        System.out.println((i + 1) + " - " + veterinarios.get(i));
    }
    int v = lerInt("Escolha o veterinário: ");
    if (v < 1 || v > veterinarios.size()) {
        System.out.println("Veterinário inválido!");
        return;
    }
    Veterinario vet = veterinarios.get(v - 1);

    
    for (Sala s : salas) {
        if (s.getVeterinario() == vet) {
            System.out.println("Esse veterinário já é responsável pela sala " + s.getNumero() + "!");
            return;
        }
    }

    Sala sala = escolherSala();
    if (sala == null) {
        System.out.println("Sala não encontrada!");
        return;
    }
    if (sala.getVeterinario() != null) {
        System.out.println("Essa sala já possui um veterinário responsável: "
                + sala.getVeterinario().getNome());
        return;
    }

    sala.setVeterinario(vet);
    System.out.println("Veterinário " + vet.getNome() + " associado à sala " + sala.getNumero() + "!");
}


private static void atribuirAtendimentoSala() {
    System.out.println("--- Atribuir atendimento a uma sala ---");
    boolean existe = false;
    for (Atendimento a : atendimentos) {
        if (a.getStatus() == StatusAtendimento.AGENDADO) {
            System.out.println(a.resumo() + " | Procedimento: " + a.getProcedimento().getNome());
            existe = true;
        }
    }
    if (!existe) {
        System.out.println("Não há atendimentos agendados.");
        return;
    }

    int codigo = lerInt("Código do atendimento: ");
    Atendimento at = buscarAtendimento(codigo);
    if (at == null || at.getStatus() != StatusAtendimento.AGENDADO) {
        System.out.println("Atendimento não encontrado ou não está agendado!");
        return;
    }

    Sala sala = escolherSala();
    if (sala == null) {
        System.out.println("Sala não encontrada!");
        return;
    }

    String erro = validarSala(sala, at, true);
    if (erro != null) {
        System.out.println(erro);
        return;
    }

    at.atribuirSala(sala);
    System.out.println("Atendimento " + at.getCodigo() + " atribuído à sala " + sala.getNumero()
            + ". Status: " + at.getStatus().getDescricao());
}


private static void exibirAtendimentosDaSala() {
    System.out.println("--- Atendimentos de uma sala ---");
    Sala sala = escolherSala();
    if (sala == null) {
        System.out.println("Sala não encontrada!");
        return;
    }

    int total = 0;
    for (Atendimento a : atendimentos) {
        if (a.getSala() == sala) {
            System.out.println(a.resumo() + " | Procedimento: " + a.getProcedimento().getNome());
            total++;
        }
    }
    System.out.println("Total de atendimentos da sala " + sala.getNumero() + ": " + total);
}


private static void totalFinalizadosPorSala() {
    System.out.println("--- Atendimentos finalizados por sala ---");
    for (Sala s : salas) {
        int count = 0;
        for (Atendimento a : atendimentos) {
            if (a.getSala() == s && a.getStatus() == StatusAtendimento.FINALIZADO) {
                count++;
            }
        }
        System.out.println("Sala " + s.getNumero() + " (Bloco " + s.getBloco() + "): " + count);
    }
}


private static void buscarPorStatus() {
    System.out.println("--- Buscar por status ---");
    System.out.println("1 - Agendado | 2 - Em andamento | 3 - Finalizado");
    int op = lerInt("Escolha: ");
    StatusAtendimento status;
    if (op == 1) status = StatusAtendimento.AGENDADO;
    else if (op == 2) status = StatusAtendimento.EM_ANDAMENTO;
    else if (op == 3) status = StatusAtendimento.FINALIZADO;
    else {
        System.out.println("Status inválido!");
        return;
    }

    boolean achou = false;
    for (Atendimento a : atendimentos) {
        if (a.getStatus() == status) {
            System.out.println("------------------------------");
            System.out.println(a.detalhes());
            achou = true;
        }
    }
    if (!achou) {
        System.out.println("Nenhum atendimento com status " + status.getDescricao() + ".");
    }
}


private static void exibirDetalhesAtendimento() {
    System.out.println("--- Detalhes do atendimento ---");
    int codigo = lerInt("Código do atendimento: ");
    Atendimento at = buscarAtendimento(codigo);
    if (at == null) {
        System.out.println("Atendimento não encontrado!");
        return;
    }
    System.out.println(at.detalhes());
}


private static void finalizarAtendimento() {
    System.out.println("--- Finalizar atendimento ---");
    int codigo = lerInt("Código do atendimento: ");
    Atendimento at = buscarAtendimento(codigo);
    if (at == null) {
        System.out.println("Atendimento não encontrado!");
        return;
    }
    if (at.getStatus() != StatusAtendimento.EM_ANDAMENTO) {
        System.out.println("Só é possível finalizar atendimentos em andamento!");
        return;
    }
    at.finalizar();
    System.out.println("Atendimento " + codigo + " finalizado!");
}


private static String validarSala(Sala sala, Atendimento novo, boolean verificarOcupacao) {
    if (!verificarOcupacao) {
        return null; 
    }

    int ocupacao = 0;
    for (Atendimento a : atendimentos) {
        if (a != novo && a.getSala() == sala && a.getStatus() == StatusAtendimento.EM_ANDAMENTO) {
            ocupacao++;
            
            if (!a.getProcedimento().getNome().equalsIgnoreCase(novo.getProcedimento().getNome())) {
                return "A sala " + sala.getNumero() + " está atendendo outro tipo de procedimento ("
                        + a.getProcedimento().getNome() + ").";
            }
        }
    }
    if (ocupacao >= sala.getCapacidadeMaxima()) {
        return "A sala " + sala.getNumero() + " está com a capacidade máxima ("
                + sala.getCapacidadeMaxima() + ") atingida.";
    }
    return null;
}


private static Atendimento buscarAtendimento(int codigo) {
    for (Atendimento a : atendimentos) {
        if (a.getCodigo() == codigo) return a;
    }
    return null;
}

private static Sala escolherSala() {
    for (Sala s : salas) {
        System.out.println(s);
    }
    int numero = lerInt("Número da sala: ");
    for (Sala s : salas) {
        if (s.getNumero() == numero) return s;
    }
    return null;
}


private static String lerTexto(String msg) {
    System.out.print(msg);
    return sc.nextLine().trim();
}

private static int lerInt(String msg) {
    while (true) {
        System.out.print(msg);
        try {
            return Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Digite um número inteiro válido.");
        }
    }
}

private static double lerDouble(String msg) {
    while (true) {
        System.out.print(msg);
        try {
            return Double.parseDouble(sc.nextLine().trim().replace(",", "."));
        } catch (NumberFormatException e) {
            System.out.println("Digite um valor válido.");
        }
    }
}

private static LocalDate lerData(String msg) {
    DateTimeFormatter f = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    while (true) {
        System.out.print(msg);
        try {
            return LocalDate.parse(sc.nextLine().trim(), f);
        } catch (Exception e) {
            System.out.println("Data inválida. Use o formato dd/MM/yyyy.");
        }
    }
}

private static LocalTime lerHora(String msg) {
    DateTimeFormatter f = DateTimeFormatter.ofPattern("HH:mm");
    while (true) {
        System.out.print(msg);
        try {
            return LocalTime.parse(sc.nextLine().trim(), f);
        } catch (Exception e) {
            System.out.println("Horário inválido. Use o formato HH:mm.");
        }
    }
}
}