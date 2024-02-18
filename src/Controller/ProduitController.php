<?php

namespace App\Controller;

use App\Form\ProduitType;
use App\Entity\Produit;
use App\Repository\ProduitRepository;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\Form\Extension\Core\Type\SubmitType;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Doctrine\ORM\EntityManagerInterface;

class ProduitController extends AbstractController
{
    #[Route('/Produit', name: 'app_produit')]
    public function index(): Response
    {
        return $this->render('Produit/index.html.twig', [
            'controller_name' => 'ProduitController',
        ]);
    }

    #[Route('/Affiche', name: 'app_affiche')]
    public function affiche(ProduitRepository $repository)
    {
        $produit = $repository->findAll(); // select *
        return $this->render('Produit/Affiche.html.twig', ['produit' => $produit]);
    }
    private $entityManager;

    public function __construct(EntityManagerInterface $entityManager)
    {
        $this->entityManager = $entityManager;
    }

    #[Route('/Add', name: 'app_add')]
    public function add(Request $request)
    {
        $produit = new Produit();
        $form = $this->createForm(ProduitType::class, $produit);
       
        $form->handleRequest($request);
        if ($form->isSubmitted() && $form->isValid()) {
            /////////////// image
            $uploadedFile = $form['img']->getData();
            $newFilename = md5(uniqid()) . '.' . $uploadedFile->guessExtension();
            $uploadDirectory = $this->getParameter('kernel.project_dir') . '/public/uploads';
            $uploadedFile->move($uploadDirectory, $newFilename);
            $produit->setImg('uploads/' . $newFilename);
            //////////// image 
            $em = $this->entityManager;
            $em->persist($produit);
            $em->flush();
            return $this->redirectToRoute('app_affiche');
        }
        return $this->render('Produit/Add.html.twig', ['form' => $form->createView()]);
    }
    #[Route('/edit/{id}', name: 'app_edit')]
    public function edit(ProduitRepository $repository, $id, Request $request)
    {
        $Produit = $repository->find($id);
        $form = $this->createForm(ProduitType::class, $Produit);
        //$form->add('Edit', SubmitType::class);

        $form->handleRequest($request);
        if ($form->isSubmitted() && $form->isValid()) {
            $em = $this->getDoctrine()->getManager();
            $em->flush(); // Correction : Utilisez la méthode flush() sur l'EntityManager pour enregistrer les modifications en base de données.
            return $this->redirectToRoute("app_affiche");
        }

        return $this->render('Produit/edit.html.twig', [
            'form' => $form->createView(),
        ]);
    }
    #[Route('/delete/{id}', name: 'app_delete')]
    public function delete($id, ProduitRepository $repository)
    {
        $Produit = $repository->find($id);

        if (!$Produit) {
            throw $this->createNotFoundException('Produit non trouvé');
        }

        $em = $this->getDoctrine()->getManager();
        $em->remove($Produit);
        $em->flush();

        
        return $this->redirectToRoute('app_affiche');
    }
}