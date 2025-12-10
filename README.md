# 🐾 Sistema de Clínica Veterinária
Projeto acadêmico de Programação Orientada a Objetos (POO) em Java.  
Sistema com GUI (Swing) para gerenciamento de Donos, Pets e Consultas veterinárias.

---

## 👥 Integrantes do Grupo
- Nome: Victor Alexandre — RA: 5168426  
- Nome: João Marcos — RA: 5168230  
- Nome: Cauã Lima — RA: 5168462  

---

## 🏗️ Arquitetura do Projeto

O sistema utiliza de forma prática os pilares da POO:

| Conceito | Onde foi aplicado |
|---------|------------------|
| Abstração | Classes abstratas `Pessoa` e `Pet` |
| Herança | `Cachorro`, `Gato`, `OutroPet`, `Dono` |
| Polimorfismo | Listas com objetos de tipo `Pet` |
| Encapsulamento | Atributos `private` + getters/setters |
| Interfaces | `Agendavel` implementada por `Consulta` |

---

## 🧩 Estrutura de Pacotes

src/

└─ com/

└─ uniube/

└─ clinica/

├─ dominio/ → Classes do modelo do domínio

├─ repositorio/ → Armazenamento em memória

└─ ui/ → Interface gráfica Swing

---

## 📌 Funcionalidades

✔ Cadastro, edição e exclusão de Donos  
✔ Cadastro, edição e exclusão de Pets com vínculo ao Dono  
✔ Registro de Consultas com data e descrição  
✔ Busca de Pets por nome ou por dono  
✔ Interface gráfica com múltiplas abas (JTabbedPane)  
✔ Dados de exemplo carregados automaticamente  

---

## 🛠️ Como Executar

### Requisitos
- Java JDK 8 ou superior

### Terminal dentro da pasta `src`
#### Compilar
```bash
javac com/uniube/clinica/dominio/*.java \
      com/uniube/clinica/repositorio/*.java \
      com/uniube/clinica/ui/*.java

Executar

java com.uniube.clinica.ui.ClinicaVeterinariaApp
