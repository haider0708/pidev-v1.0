<?php

namespace App\Form;

use App\Entity\PATIENT;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\Extension\Core\Type\CheckboxType;
use Symfony\Component\Form\Extension\Core\Type\PasswordType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;
use Symfony\Component\Validator\Constraints\IsTrue;
use Symfony\Component\Validator\Constraints\Length;
use Symfony\Component\Validator\Constraints\NotBlank;
use Symfony\Component\Form\Extension\Core\Type\FileType;
use Symfony\Component\Form\Extension\Core\Type\ChoiceType;
use Symfony\Component\Form\Extension\Core\Type\TextType;
use Symfony\Component\Form\Extension\Core\Type\IntegerType;
class RegistrationFormType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
{
    $builder
        ->add('firstname', TextType::class, [
            'attr' => [
                'placeholder' => 'Name',
            ],
            'row_attr' => [
                'style' => 'font-size: 12px;', // Adjust the font size as needed
            ],
        ])
        ->add('lastname', TextType::class, [
            'attr' => [
                'placeholder' => 'L.Name',
            ],
            'row_attr' => [
                'style' => 'font-size: 12px;', // Adjust the font size as needed
            ],
        ])
        ->add('email', TextType::class, [
            'attr' => [
                'placeholder' => 'Email',
            ],
            'row_attr' => [
                'style' => 'font-size: 12px;', // Adjust the font size as needed
            ],
        ])
        ->add('plainPassword', PasswordType::class, [
            'mapped' => false,
            'attr' => [
                'autocomplete' => 'new-password',
                'placeholder' => 'Password',
            ],
            'constraints' => [
                new NotBlank([
                    'message' => 'Please enter a password',
                ]),
                new Length([
                    'min' => 6,
                    'minMessage' => 'Your password should be at least {{ limit }} characters',
                    'max' => 4096,
                ]),
            ],
            'row_attr' => [
                'style' => 'font-size: 12px;', // Adjust the font size as needed
            ],
        ])
        ->add('sexe', ChoiceType::class, [
            'choices' => [
                'Male' => 'male',
                'Female' => 'female',
            ],
            'placeholder' => 'Choose your gender',
            'required' => true,
            'label' => 'Gender',
            'row_attr' => [
                'style' => 'font-size: 12px;', // Adjust the font size as needed
            ],
        ])
        ->add('age', IntegerType::class, [
            'attr' => [
                'placeholder' => 'Age',
            ],
            'row_attr' => [
                'style' => 'font-size: 12px;', // Adjust the font size as needed
            ],
        ])
        ->add('number', TextType::class, [
            'attr' => [
                'placeholder' => 'Number',
            ],
            'row_attr' => [
                'style' => 'font-size: 12px;', // Adjust the font size as needed
            ],
        ])
        ->add('img_path', FileType::class, [
            'label' => 'Image (JPG, PNG, GIF)',
            'mapped' => false,
            'required' => false,
            'attr' => [
                'placeholder' => 'Choose file',
            ],
            'row_attr' => [
                'style' => 'font-size: 12px;', // Adjust the font size as needed
            ],
        ])
        ->add('address', TextType::class, [
            'attr' => [
                'placeholder' => 'Address',
            ],
            'row_attr' => [
                'style' => 'font-size: 12px;', // Adjust the font size as needed
            ],
        ])
    ;
}


    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'data_class' => PATIENT::class,
        ]);
    }
}
