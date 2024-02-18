<?php

namespace App\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

class FrontController extends AbstractController
{
    #[Route('/', name: 'app_front')]
    public function index(EventRepository $eventRepository): Response
    {
        return $this->render('front/indexFront.html.twig', [
            'events' => $eventRepository->findAll(),
        ]);
    }
}
