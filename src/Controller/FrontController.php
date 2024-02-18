<?php

namespace App\Controller;
use App\Repository\PharmacieRepository;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

class FrontController extends AbstractController
{
    #[Route('/front', name: 'app_front')]
    public function index(): Response
    {
        return $this->render('front/index.html.twig', [
            'controller_name' => 'FrontController',
        ]);
    } 
    #[Route('/Affichefr', name: 'app_Affichefr')]
    public function Affiche (PharmacieRepository $repository)
        {
            $pharmacie=$repository->findAll() ; //select *
            //return $this->render('GestionPharmacie/Affiche.html.twig',['pharmacie'=>$pharmacie]);
            return $this->render('front/affichefront.html.twig',['pharmacie'=>$pharmacie]);
        }
}
