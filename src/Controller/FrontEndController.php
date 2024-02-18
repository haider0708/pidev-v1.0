<?php

namespace App\Controller;
use App\Repository\ProduitRepository;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Doctrine\ORM\EntityManagerInterface;

class FrontEndController extends AbstractController
{
    #[Route('/front', name: 'app_front_end')]
    public function index(): Response
    {
        return $this->render('front_end/index.html.twig', [
            'controller_name' => 'FrontEndController',
        ]);
    }
    #[Route('/Affichef', name: 'app_affichef')]
    public function affiche(ProduitRepository $repository)
    {
        $produit = $repository->findAll(); // select *
        //return $this->render('Produit/Affichef.html.twig', ['produit' => $produit]);
         return $this->render('front_end/affichef.html.twig', ['produit' => $produit]);
    }
    private $entityManager;

    public function __construct(EntityManagerInterface $entityManager)
    {
        $this->entityManager = $entityManager;
    }
 
}