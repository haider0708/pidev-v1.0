package models;

public class Produit {
    int id;
    private String nom;
    private float prix;
    private String description;
    private int categorie_id;
    private String nomcategorie;


    // Constructeur
    public Produit() {}
    public Produit(String nom, float prix, String description,int categorie_id,String nomcategorie) {
        this.nom = nom;
        this.prix = prix;
        this.description = description;
        this.categorie_id = categorie_id;
        this.nomcategorie = nomcategorie;
    }

    // Méthodes d'accès (getters) et de modification (setters)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public float getPrix() {
        return prix;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;

    }
    public String getNomcategorie() {
        return nomcategorie;
    }

    public void setNomcategorie(String nomcategorie) {
        this.nomcategorie = nomcategorie;
    }
    @Override
    public String toString() {
        return "Produit{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", description='" + description+ '\'' +
                ", prix=" + prix +'\''  +
                ", categorie_id=" + categorie_id +'\''  +
                ", nomcategorie=" + nomcategorie +
                '}';
    }
}
