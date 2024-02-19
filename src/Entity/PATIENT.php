<?php

namespace App\Entity;

use App\Repository\PATIENTRepository;
use Doctrine\ORM\Mapping as ORM;
use Symfony\Bridge\Doctrine\Validator\Constraints\UniqueEntity;
use Symfony\Component\Security\Core\User\PasswordAuthenticatedUserInterface;
use Symfony\Component\Security\Core\User\UserInterface;

use Symfony\Component\Validator\Constraints as Assert;

#[ORM\Entity(repositoryClass: PATIENTRepository::class)]
#[UniqueEntity(fields: ['email'], message: 'There is already an account with this email')]
class PATIENT implements UserInterface, PasswordAuthenticatedUserInterface
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    private ?int $id = null;


    #[ORM\Column(length: 180, unique: true)]
    #[Assert\NotBlank]
    #[Assert\Email( message: 'The email {{ value }} is not a valid email.',)]
    private ?string $email = null;

    #[ORM\Column]
    private array $roles = [];

    /**
     * @var string The hashed password
     */
    #[ORM\Column]
    private ?string $password = null;

    
    #[ORM\Column(length: 255)]
    #[Assert\NotBlank]
    #[Assert\Length(min: 4,minMessage: "veuillez avoir au minimum 4 caractere" )]
    #[Assert\Regex(
        pattern: '/\d/',
        match: false,
        message: 'Your name cannot contain a number',)]

    private ?string $firstname = null;

    #[ORM\Column(length: 255)]
    #[Assert\NotBlank]
    #[Assert\Length(min: 4,minMessage: "veuillez avoir au minimum 4 caractere" )]
    #[Assert\Regex(
        pattern: '/\d/',
        match: false,
        message: 'Your name cannot contain a number',)]

    private ?string $lastname = null;

    #[ORM\Column(length: 255)]
    private ?string $sexe = null;

    #[ORM\Column]
    #[Assert\NotBlank]
    #[Assert\Length(max: 3,maxMessage: "veuillez avoir au maximum 3 chiffre" )]
    private ?int $age = null;

    #[ORM\Column(length: 255)]
    #[Assert\NotBlank]
    #[Assert\Length(min: 8 , max: 8 ,minMessage: "votre numero doit etre 8 chiffre",maxMessage: "votre numero doit etre 8 chiffre" )]
    private ?string $number = null;

    #[ORM\Column(length: 255)]
    private ?string $img_path = null;

    #[ORM\Column(length: 255)]
    #[Assert\NotBlank]
    private ?string $address = null;

    public function getId(): ?int
    {
        return $this->id;
    }

    public function getEmail(): ?string
    {
        return $this->email;
    }

    public function setEmail(string $email): static
    {
        $this->email = $email;

        return $this;
    }

    /**
     * A visual identifier that represents this user.
     *
     * @see UserInterface
     */
    public function getUserIdentifier(): string
    {
        return (string) $this->email;
    }

    /**
     * @deprecated since Symfony 5.3, use getUserIdentifier instead
     */
    public function getUsername(): string
    {
        return (string) $this->email;
    }

    /**
     * @see UserInterface
     */
    public function getRoles(): array
    {
        $roles = $this->roles;
        // guarantee every user at least has ROLE_USER
        $roles[] = 'ROLE_USER';

        return array_unique($roles);
    }

    public function setRoles(array $roles): static
    {
        $this->roles = $roles;

        return $this;
    }

    /**
     * @see PasswordAuthenticatedUserInterface
     */
    public function getPassword(): string
    {
        return $this->password;
    }

    public function setPassword(string $password): static
    {
        $this->password = $password;

        return $this;
    }

    /**
     * Returning a salt is only needed, if you are not using a modern
     * hashing algorithm (e.g. bcrypt or sodium) in your security.yaml.
     *
     * @see UserInterface
     */
    public function getSalt(): ?string
    {
        return null;
    }

    /**
     * @see UserInterface
     */
    public function eraseCredentials(): void
    {
        // If you store any temporary, sensitive data on the user, clear it here
        // $this->plainPassword = null;
    }

    public function getFirstname(): ?string
    {
        return $this->firstname;
    }

    public function setFirstname(string $firstname): static
    {
        $this->firstname = $firstname;

        return $this;
    }

    public function getLastname(): ?string
    {
        return $this->lastname;
    }

    public function setLastname(string $lastname): static
    {
        $this->lastname = $lastname;

        return $this;
    }

    public function getSexe(): ?string
    {
        return $this->sexe;
    }

    public function setSexe(string $sexe): static
    {
        $this->sexe = $sexe;

        return $this;
    }

    public function getAge(): ?int
    {
        return $this->age;
    }

    public function setAge(int $age): static
    {
        $this->age = $age;

        return $this;
    }

    public function getNumber(): ?string
    {
        return $this->number;
    }

    public function setNumber(string $number): static
    {
        $this->number = $number;

        return $this;
    }

    public function getimg_path(): ?string
    {
        return $this->img_path;
    }

    public function setimg_path(string $img_path): static
    {
        $this->img_path = $img_path;

        return $this;
    }

    public function getAddress(): ?string
    {
        return $this->address;
    }

    public function setAddress(string $address): static
    {
        $this->address = $address;

        return $this;
    }
}
