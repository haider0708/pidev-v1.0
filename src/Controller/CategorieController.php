<?php

namespace App\Controller;
use App\Form\CategorieType;
use App\Entity\Categorie;
use App\Repository\CategorieRepository;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\Form\Extension\Core\Type\SubmitType;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\Routing\Annotation\Route;
use Doctrine\ORM\EntityManagerInterface;


class CategorieController extends AbstractController
{
    #[Route('/categorie', name: 'app_categorie')]
    public function index(): Response
    {
        return $this->render('categorie/index.html.twig', [
            'controller_name' => 'CategorieController',
            
        ]);
    }
    #[Route('/Affichec', name: 'app_affichec')]
    public function affiche(CategorieRepository $repository)
    {
        $categorie = $repository->findAll(); // select *
        $categoriesCount = $repository->count([]);
        return $this->render('categorie/Affiche.html.twig', [
            'categorie' => $categorie,
            'categoriesCount' => $categoriesCount,
        ]);
        
    }
    private $entityManager;

    public function __construct(EntityManagerInterface $entityManager)
    {
        $this->entityManager = $entityManager;
    }
    #[Route('/Addc', name: 'app_addc')]
    public function add(Request $request)
    {
        $Categorie = new Categorie();
        $form = $this->createForm(CategorieType::class, $Categorie);
       // $form->add('Ajouter', SubmitType::class);
        $form->handleRequest($request);
        if ($form->isSubmitted() && $form->isValid()) {
            $em = $this->entityManager;
            $em->persist($Categorie);
            $em->flush();
            return $this->redirectToRoute('app_affichec');
        }
        return $this->render('Categorie/Add.html.twig', ['form' => $form->createView()]);
    }
    #[Route('/editc/{id}', name: 'app_editc')]
    public function edit(CategorieRepository $repository, $id, Request $request)
    {
        $categorie = $repository->find($id);
        $form = $this->createForm(CategorieType::class, $categorie);
       // $form->add('Edit', SubmitType::class);

        $form->handleRequest($request);
        if ($form->isSubmitted() && $form->isValid()) {
            $em = $this->getDoctrine()->getManager();
            $em->flush(); // Correction : Utilisez la méthode flush() sur l'EntityManager pour enregistrer les modifications en base de données.
            return $this->redirectToRoute("app_affichec");
        }

        return $this->render('Categorie/edit.html.twig', [
            'form' => $form->createView(),
        ]);
    }
    #[Route('/deletec/{id}', name: 'app_deletec')]
    public function delete($id, CategorieRepository $repository)
    {
        $categorie = $repository->find($id);

        if (!$categorie) {
            throw $this->createNotFoundException('Produit non trouvé');
        }

        $em = $this->getDoctrine()->getManager();
        $em->remove($categorie);
        $em->flush();

        
        return $this->redirectToRoute('app_affichec');
    }
    #[Route('/statistiques', name: 'app_statistiques')]
    public function index1(CategorieRepository $categorieRepository): Response
    {
        $categoriesCount = $categorieRepository->count([]);
        // Autres calculs de statistiques peuvent être effectués ici

        return $this->render('statistique/index.html.twig', [
            'categoriesCount' => $categoriesCount,
            // Autres données statistiques peuvent être transmises au template ici
        ]);
        return $this->redirectToRoute('app_affichec');
    }

}
