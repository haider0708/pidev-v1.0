<?php

namespace App\Entity;
use Symfony\Component\Validator\Constraints as Assert;
use App\Repository\RapportRepository;
use Doctrine\ORM\Mapping as ORM;

#[ORM\Entity(repositoryClass: RapportRepository::class)]
class Rapport
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]



    private ?int $id = null;



     /**
     * @Assert\NotBlank(message="La note ne peut pas être vide.")
     * @Assert\Regex(
     *     pattern="/^[a-zA-Z\s]+$/",
     *     message="La note ne doit contenir que des lettres et des espaces."
     * )
     */
    #[ORM\Column(length: 255)]
    private ?string $note = null;


     /**
     * @Assert\NotBlank(message="Le type ne peut pas être vide.")
     * @Assert\Choice(
     *     choices={"diabetique", "consultation_psychologique", "resultat_analyses"},
     *     message="Le type doit être l'un des choix proposés."
     * )
     */
    #[ORM\Column(length: 255)]
    private ?string $type = null;

    #[ORM\OneToOne(cascade: ['persist', 'remove'])]
    private ?Rendezvous $rdv = null;

    public function getId(): ?int
    {
        return $this->id;
    }

    public function getNote(): ?string
    {
        return $this->note;
    }

    public function setNote(string $note): static
    {
        $this->note = $note;

        return $this;
    }

    public function getType(): ?string
    {
        return $this->type;
    }

    public function setType(string $type): static
    {
        $this->type = $type;

        return $this;
    }

    public function getRdv(): ?Rendezvous
    {
        return $this->rdv;
    }

    public function setRdv(?Rendezvous $rdv): static
    {
        $this->rdv = $rdv;

        return $this;
    }
}
