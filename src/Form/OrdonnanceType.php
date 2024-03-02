<?php

namespace App\Form;
use Symfony\Component\Form\Extension\Core\Type\ChoiceType;
use App\Entity\Ordonnance;
use App\Entity\Pharmacie;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;
use Symfony\Component\Form\Extension\Core\Type\DateTimeType;
use Symfony\Component\Form\Extension\Core\Type\DateType;
use Symfony\Component\Form\Extension\Core\Type\TextType;
use Symfony\Component\Validator\Constraints\GreaterThanOrEqual;
use Symfony\Component\Validator\Constraints\NotBlank;
use Symfony\Bridge\Doctrine\Form\Type\EntityType;


class OrdonnanceType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
            
            ->add('nommedecin', null, [
                'constraints' => [
                    new NotBlank(['message' => "Le champ 'nom' ne peut pas être vide."]),
                ],
            ])
            ->add('nompatient')
            ->add('description')
            //->add('datedecreation', DateType::class, [
              //  'widget' => 'single_text', // Afficher en tant qu'entrée texte unique
                //'format' => 'yyyy-MM-dd', // Définir le format de date souhaité
                //'required' => false,
                //'data' => new \DateTime(),
           // ])
            ->add('datedecreation', DateType::class, [
                'widget' => 'single_text',
                'constraints' => [
                    new GreaterThanOrEqual([
                        'value' => new \DateTime('today midnight'),
                        'message' => 'La date ne peut pas être dans le passé',
                    ]),
                ],
            ])
            ->add('pharmacie', EntityType::class, [
                'class' => Pharmacie::class,
                'choice_label' => 'nom', // Le champ de la pharmacie à afficher dans la liste déroulante
                'placeholder' => 'Sélectionner une pharmacie',
                'required' => true,
            ]);
           
       
        ;
    }

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'data_class' => Ordonnance::class,
        ]);
    }
}