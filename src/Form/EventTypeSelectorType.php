<?php

namespace App\Form;

use App\Entity\Event;
use App\Repository\EventRepository;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\Extension\Core\Type\ChoiceType;
use Symfony\Component\Form\Extension\Core\Type\EmailType;
use Symfony\Component\Form\Extension\Core\Type\SubmitType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;

class EventTypeSelectorType extends AbstractType
{
    private $eventRepository;

    public function __construct(EventRepository $eventRepository)
    {
        $this->eventRepository = $eventRepository;
    }

    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $events = $this->eventRepository->findAll();
        $eventsChoices = [];
        foreach ($events as $event) {
            $eventsChoices[$event->getTitre()] = $event->getId();
        }

        $builder
            ->add('event', ChoiceType::class, [
                'choices' => $eventsChoices,
                'label' => 'Select Event',
            ])
            ->add('email', EmailType::class, [
                'label' => 'Your Email',
            ])
            ->add('send', SubmitType::class, [
                'label' => 'Send Invitation',
            ]);
    }

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            // Configure your default options here
        ]);
    }
}
