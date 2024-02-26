<?php

namespace App\Entity;

use App\Repository\LivreurRepository;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;
use Doctrine\ORM\Mapping as ORM;

use Symfony\Component\Validator\Constraints as Assert;

use Symfony\Component\Serializer\Annotation\Groups;

#[ORM\Entity(repositoryClass: LivreurRepository::class)]

class Livreur
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]

    #[Groups("livreurs")]
    private ?int $id = null;

    #[ORM\Column(length: 255)]
    #[Assert\NotBlank]
    #[Assert\Length(min: 4,minMessage: "veuillez avoir au minimum 4 caractere" )]
    #[Assert\Regex(
        pattern: '/\d/',
        match: false,
        message: 'Your name cannot contain a number',)]
        #[Groups("livreurs")]
       
    private ?string $nom = null;

    #[ORM\Column(length: 255)]
    #[Assert\NotBlank]
    #[Assert\Length(min: 4,minMessage: "veuillez avoir au minimum 4 caractere" )]


    #[Assert\Regex(
        pattern: '/\d/',
        match: false,
        message: 'Your prenom cannot contain a number',
    )]
    #[Groups("livreurs")]
    private ?string $prenom = null;

    #[ORM\Column]

    #[Groups("livreurs")]
    private ?int $numeroTel = null;

    #[ORM\OneToMany(mappedBy: 'livreur', targetEntity: Commande::class,cascade: ['persist','remove'])]
    
   
    private Collection $commande;

    #[ORM\Column(length: 255, nullable: true)]
    #[Groups("livreurs")]
    private ?string $image = null;

    public function __construct()
    {
        $this->commande = new ArrayCollection();
    }

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

    public function getPrenom(): ?string
    {
        return $this->prenom;
    }

    public function setPrenom(string $prenom): static
    {
        $this->prenom = $prenom;

        return $this;
    }

    public function getNumeroTel(): ?int
    {
        return $this->numeroTel;
    }

    public function setNumeroTel(int $numeroTel): static
    {
        $this->numeroTel = $numeroTel;

        return $this;
    }

    /**
     * @return Collection<int, Commande>
     */
    public function getCommande(): Collection
    {
        return $this->commande;
    }

    public function addCommande(Commande $commande): static
    {
        if (!$this->commande->contains($commande)) {
            $this->commande->add($commande);
            $commande->setLivreur($this);
        }

        return $this;
    }

    public function removeCommande(Commande $commande): static
    {
        if ($this->commande->removeElement($commande)) {
            // set the owning side to null (unless already changed)
            if ($commande->getLivreur() === $this) {
                $commande->setLivreur(null);
            }
        }

        return $this;
    }
    // public function __toString(): string
    // {
    //     return $this->nom. " " .$this->prenom. " " .$this->numeroTel." ";
    // }

    public function getImage(): ?string
    {
        return $this->image;
    }

    public function setImage(?string $image): static
    {
        $this->image = $image;

        return $this;
    }
    public function __toString(): string
    {
        $commandesString = '';
        foreach ($this->commande as $commande) {
            $commandesString .= $commande->__toString() . ', ';
        }
        return "ID: {$this->id}, Nom: {$this->nom}, Prénom: {$this->prenom}, Numéro de téléphone: {$this->numeroTel}, Commandes: [{$commandesString}]";
    }
}
