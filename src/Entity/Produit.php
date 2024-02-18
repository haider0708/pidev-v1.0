<?php

namespace App\Entity;

use App\Repository\ProduitRepository;
use Doctrine\ORM\Mapping as ORM;
use Symfony\Component\Validator\Constraints as Assert;
use Symfony\Component\HttpFoundation\File\File;
#[ORM\Entity(repositoryClass: ProduitRepository::class)]
class Produit
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    private ?int $id = null;

    #[ORM\Column(length: 255)]
    private ?string $nom = null;

    #[ORM\Column(length: 255)]
        /**
     * @Assert\NotBlank(message="description  doit etre non vide")
     * @Assert\Length(
     *      min = 7,
     *      max = 100,
     *      minMessage = "doit etre >=7 ",
     *      maxMessage = "doit etre <=100" )
     * @ORM\Column(type="string", length=1000)
     */
    private ?string $description = null;


    #[ORM\Column]
    private ?float $prix = null;

    #[ORM\Column]
    private ?string $quantite = null;

    #[ORM\Column(length: 255, nullable: true)]
    private ?string $img_path = null;

    #[ORM\Column(length: 255)]
    private ?string $img = null;

    #[ORM\ManyToOne(targetEntity: Categorie::class, inversedBy: 'produits')]
    private ?Categorie $categorie = null;

    
    


    public function getId(): ?int
    {
        return $this->id;
    }

    public function getNom(): ?string
    {
        return $this->nom;
    }

    public function setNom(string $nom): static
    {
        $this->nom = $nom;

        return $this;
    }

    public function getDescription(): ?string
    {
        return $this->description;
    }

    public function setDescription(string $description): static
    {
        $this->description = $description;

        return $this;
    }

    public function getPrix(): ?float
    {
        return $this->prix;
    }

    public function setPrix(float $prix): static
    {
        $this->prix = $prix;

        return $this;
    }

    public function getQuantite(): ?string
    {
        return $this->quantite;
    }

    public function setQuantite(string $quantite): static
    {
        $this->quantite = $quantite;

        return $this;
    }

    public function getImgPath(): ?string
    {
        return $this->img_path;
    }

    public function setImgPath(?string $img_path): static
    {
        $this->img_path = $img_path;

        return $this;
    }

    public function getImg(): ?string
    {
        return $this->img;
    }

    public function setImg(string $img): static
    {
        $this->img = $img;

        return $this;
    }

    public function getCategorie(): ?Categorie
    {
        return $this->categorie;
    }

    public function setCategorie(?Categorie $Categorie): static
    {
        $this->categorie = $Categorie;

        return $this;
    }
}
