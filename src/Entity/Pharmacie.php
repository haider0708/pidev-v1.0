<?php

namespace App\Entity;

use App\Repository\PharmacieRepository;
use Doctrine\ORM\Mapping as ORM;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;
use Symfony\Component\Validator\Constraints as Assert;
use Symfony\Component\HttpFoundation\File\File;

#[ORM\Entity(repositoryClass: PharmacieRepository::class)]
class Pharmacie
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    private ?int $id = null;
    //nom pharmacie
    #[ORM\Column]
      /**
     * @Assert\NotBlank(message="Le nom doit être non vide")
     * @Assert\Length(
     *      min = 2,
     *      minMessage="Entrer un titre au minimum de 2 caractères"
     * )
     */
    private ?string $nom = null;
    //adresse
    #[ORM\Column]
    private ?string $adresse = null;
    //num tel
    #[ORM\Column]
    private ?string $numerotelephone = null;
    //image
    #[ORM\Column(length: 255)]
    private ?string $image = null;
   
    #[ORM\OneToMany(mappedBy: 'pharmacie', targetEntity: Ordonnance::class)]
    private Collection $ordonnances;

   

    

    public function __construct()
    {
        $this->ordonnances = new ArrayCollection();
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
    public function getAdresse(): ?string
    {
        return $this->adresse;
    }

    public function setAdresse(string $adresse): static
    {
        $this->adresse = $adresse;

        return $this;
    }
    public function getNumerotelephone(): ?string
    {
        return $this->numerotelephone;
    }

    public function setNumerotelephone(string $numerotelephone): static
    {
        $this->numerotelephone = $numerotelephone;

        return $this;
    }

    public function getImage(): ?string
    {
        return $this->image;
    }

    public function setImage(?string $image): static
    {
        $this->image = $image;

        return $this;
    }




}
