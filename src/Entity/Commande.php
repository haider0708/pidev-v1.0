<?php

namespace App\Entity;

use App\Repository\CommandeRepository;
use Doctrine\ORM\Mapping as ORM;


#[ORM\Entity(repositoryClass: CommandeRepository::class)]
class Commande
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    private ?int $id = null;

    #[ORM\Column(length: 255)]
    private ?string $nomClient = null;

    #[ORM\Column(length: 255)]
    private ?string $addresseClient = null;

    #[ORM\Column]
    private ?int $numeroClient = null;

    #[ORM\ManyToOne(inversedBy: 'commande')]
    
   
    private ?Livreur $livreur = null;

    public function getId(): ?int
    {
        return $this->id;
    }

    public function getNomClient(): ?string
    {
        return $this->nomClient;
    }

    public function setNomClient(string $nomClient): static
    {
        $this->nomClient = $nomClient;

        return $this;
    }

    public function getAddresseClient(): ?string
    {
        return $this->addresseClient;
    }

    public function setAddresseClient(string $addresseClient): static
    {
        $this->addresseClient = $addresseClient;

        return $this;
    }

    public function getNumeroClient(): ?int
    {
        return $this->numeroClient;
    }

    public function setNumeroClient(int $numeroClient): static
    {
        $this->numeroClient = $numeroClient;

        return $this;
    }

    public function getLivreur(): ?Livreur
    {
        return $this->livreur;
    }

    public function setLivreur(?Livreur $livreur): static
    {
        $this->livreur = $livreur;

        return $this;
    }
}
