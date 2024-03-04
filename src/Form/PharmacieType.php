<?php
namespace App\Form;
use App\Entity\Ordonnance;
use App\Entity\Pharmacie;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\Extension\Core\Type\FileType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;
use Symfony\Component\Validator\Constraints\File;
use Symfony\Component\Validator\Constraints\NotBlank;
use Symfony\Component\Validator\Constraints\Regex;
use Symfony\Component\Form\Extension\Core\Type\CollectionType;
use Symfony\Component\Form\Extension\Core\Type\IntegerType;

class PharmacieType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
            ->add('nom', null, [
                'constraints' => [
                    new NotBlank(['message' => "Le champ 'nom' ne peut pas être vide."]),
                ],
            ])
            ->add('adresse', null, [
                'constraints' => [
                    new NotBlank(['message' => "Le champ 'adresse' ne peut pas être vide."]),
                ],
            ])
            ->add('numerotelephone', null, [
                'constraints' => [
                    new NotBlank(['message' => "Le champ 'numéro de téléphone' ne peut pas être vide."]),
                   /* new Regex([
                        'pattern' => '^[a-z]+$/i',
                        'message' => "Le numéro de téléphone ne peut contenir que des chiffres.",
                    ]),*/
                ],
            ])
            ->add('image', FileType::class, [
                'mapped' => false,
                'constraints' => [
                    new File([
                        'maxSize' => '5M',
                        'mimeTypes' => ['image/jpeg', 'image/png', 'image/gif'],
                        'mimeTypesMessage' => "Veuillez télécharger une image au format JPEG, PNG ou GIF.",
                    ]),
                ],
                
            ])
            ->add('note', IntegerType::class, [
               'label' => 'Note de la pharmacie',
                'required' => false, // ou true selon vos besoins
                'attr' => [
                    'min' => 1,
                    'max' => 5,
                    // Autres attributs selon vos besoins
                ],])
          ; 
    }

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'data_class' => Pharmacie::class,
        ]);
    }
}
