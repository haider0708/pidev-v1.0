<?php

namespace App\Form;

use App\Entity\Produit;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;
use Symfony\Component\Validator\Constraints\File;
use Symfony\Component\Validator\Constraints\NotBlank;
use Symfony\Component\Validator\Constraints\PositiveOrZero;
use Symfony\Component\Form\Extension\Core\Type\FileType;

class ProduitType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
            ->add('nom', null, [
                'constraints' => [
                    new NotBlank(['message' => "Le champ 'nom' ne peut pas être vide."]),
                ],
            ])
            ->add('description', null, [
                'constraints' => [
                    new NotBlank(['message' => "Le champ 'description' ne peut pas être vide."]),
                ],
            ])
            ->add('prix', null, [
                'constraints' => [
                    new NotBlank(['message' => "Le champ 'prix' ne peut pas être vide."]),
                    new PositiveOrZero(['message' => "Le prix doit être un nombre positif ou zéro."]),
                ],
            ])
            ->add('categorie', null, [
                'constraints' => [
                    new NotBlank(['message' => "Le champ 'categorie' ne peut pas être vide."]),
                ],
            ])
            ->add('quantite', null, [
                'constraints' => [
                    new NotBlank(['message' => "Le champ 'quantite' ne peut pas être vide."]),
                    new PositiveOrZero(['message' => "La quantité doit être un nombre positif ou zéro."]),
                ],
            ])
            ->add('img', FileType::class, [
                'mapped' => false,
                'constraints' => [
                    new File([
                        'maxSize' => '5M',
                        'mimeTypes' => ['image/jpeg', 'image/png', 'image/gif'],
                        'mimeTypesMessage' => "Veuillez télécharger une image au format JPEG, PNG ou GIF.",
                    ]),
                ],
            ]);
    }

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'data_class' => Produit::class,
        ]);
    }
}