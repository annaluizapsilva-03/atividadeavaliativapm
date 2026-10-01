public class Sala { private int numero; private String bloco; private int capacidadeMaxima; private String tipo; private Veterinario veterinario; 
public Sala(int numero, String bloco, int capacidadeMaxima, String tipo) {
    this.numero = numero;
    this.bloco = bloco;
    this.capacidadeMaxima = capacidadeMaxima;
    this.tipo = tipo;
}

public int getNumero() { return numero; }
public String getBloco() { return bloco; }
public int getCapacidadeMaxima() { return capacidadeMaxima; }
public String getTipo() { return tipo; }
public Veterinario getVeterinario() { return veterinario; }

public void setVeterinario(Veterinario veterinario) {
    this.veterinario = veterinario;
}

@Override
public String toString() {
    String vet = (veterinario != null) ? veterinario.getNome() : "Sem veterinário";
    return "Sala " + numero + " | Bloco " + bloco + " | Capacidade: " + capacidadeMaxima
            + " | Tipo: " + tipo + " | Responsável: " + vet;
}
}