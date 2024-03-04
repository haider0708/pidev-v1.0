<?php

namespace App\Entity;

use App\Repository\OrdonnanceRepository;
use Symfony\Component\Validator\Constraints as Assert;
use Doctrine\ORM\Mapping as ORM;
use Doctrine\DBAL\Types\Types;
use DateTime;

#[ORM\Entity(repositoryClass: OrdonnanceRepository::class)]
class Ordonnance
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    private ?int $id = null;
    #[ORM\Column]
      /**
     * @Assert\NotBlank(message="")
     * @Assert\Length(
     *      min = 2,
     *      minMessage="Entrer un titre au minimum de 2 caractères"
     * )
     */
    private ?string $nommedecin = null;
    #[ORM\Column]
       /**
     * @Assert\NotBlank(message="Le nom doit être non vide")
     * @Assert\Length(
     *      min = 2,
     *      minMessage="Entrer un titre au minimum de 2 caractères"
     * )
     */
    private ?string $nompatient = null;
    #[ORM\Column]
       /**
     * @Assert\NotBlank(message="La description doit être non vide")
     * @Assert\Length(
     *      min = 2,
     *      minMessage="Entrer un titre au minimum de 2 caractères"
     * )
     */
    private ?string $description = null;

    #[ORM\Column(type: Types::DATE_MUTABLE)]
    #[Assert\NotBlank(message: 'La date ne peut pas être vide')]
    #[Assert\Range(
        min: 'today',
        minMessage: 'La date ne peut pas être dans le passé',
        max: 'today',
        maxMessage: 'La date ne peut pas être dans le futur'
    )]
    private ?\DateTimeInterface $datedecreation = null;
    #[ORM\ManyToOne(inversedBy: 'pharmacies')]
    private ?Pharmacie $pharmacie = null;

    public function __construct()
    {
        $this->datedecreation = new DateTime();
    }
    public function getId(): ?int
    {
        return $this->id;
    }
    public function getNommedecin(): ?string
    {
        return $this->nommedecin;
    }

    public function setNommedecin(string $nommedecin): static
    {
        $this->nommedecin = $nommedecin;

        return $this;
    }
    public function getNompatient(): ?string
    {
        return $this->nompatient;
    }

    public function setNompatient(string $nompatient): static
    {
        $this->nompatient = $nompatient;

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
    public function getDatedecreation(): ?\DateTimeInterface
    {
        return $this->datedecreation;
    }

    public function setDatedecreation(\DateTimeInterface $datedecreation): static
    {
        $this->datedecreation = $datedecreation;

        return $this;
    }
    
    
    public function getPharmacie(): ?Pharmacie
    {
        return $this->pharmacie;
    }
    
    public function setPharmacie(?Pharmacie $pharmacie): static
    {
        $this->pharmacie = $pharmacie;
    
        return $this;
    }
    
   
}

