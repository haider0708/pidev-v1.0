<?php

namespace App\Entity;

use App\Repository\RendezvousRepository;
use Doctrine\ORM\Mapping as ORM;
use Symfony\Component\Validator\Constraints as Assert;

#[ORM\Entity(repositoryClass: RendezvousRepository::class)]
class Rendezvous
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]


   
    private ?int $id = null;
    #[ORM\Column]
     

    private ?\DateTime $date = null;
    #[ORM\Column(length: 255)]
     
/**
 * @Assert\NotBlank(message="Le lieu ne peut pas être vide.")
 * @Assert\Length(
 *      min=2,
 *      max=255,
 *      minMessage="Le lieu doit contenir au moins {{ limit }} caractères.",
 *      maxMessage="Le lieu ne peut pas dépasser {{ limit }} caractères."
 * )
 */

    private ?string $lieu = null;
    #[ORM\Column(length: 255)]
    /**
     * @Assert\Length(
     *      min=2,
     *      max=255,
     *      exactMessage="La note doit avoir exactement {{ limit }} caracteres."
     * )
     */
    private ?string $description = null;

    public function getId(): ?int
    {
        return $this->id;
    }


    public function getDate(): ?\DateTime
    {
        return $this->date;
    }

    public function setDate(?\DateTime $date): void
    {
        $this->date = $date;

    }

    public function getLieu(): ?string
    {
        return $this->lieu;
    }

    public function setLieu(string $email): static
    {
        $this->lieu = $email;

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
    public function __toString()
    {
        // Return a string representation of the object
        return 'Rendezvous object';
    }
}
    



