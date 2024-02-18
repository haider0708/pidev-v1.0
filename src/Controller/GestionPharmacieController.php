<?php

namespace App\Controller;

use App\Form\PharmacieType;
use App\Entity\Pharmacie;
use App\Repository\PharmacieRepository;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\Form\Extension\Core\Type\SubmitType;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Doctrine\ORM\EntityManagerInterface;
//pharmacie
class GestionPharmacieController extends AbstractController
{
    #[Route('/gestion/pharmacie', name: 'app_gestion_pharmacie')]
    public function index(): Response
    {
        return $this->render('GestionPharmacie/index.html.twig', [
            'controller_name' => 'GestionPharmacieController',
        ]);
    }
    #[Route('/Affichep', name: 'app_Affichep')]


    public function Affiche (PharmacieRepository $repository)
        {
            $pharmacie=$repository->findAll() ; //select *
            return $this->render('GestionPharmacie/Affiche.html.twig',['pharmacie'=>$pharmacie]);
        }
        private $entityManager;

    public function __construct(EntityManagerInterface $entityManager)
    {
        $this->entityManager = $entityManager;
    }
        #[Route('/Addp', name: 'app_Addp')]

        public function  Add (Request  $request)
        {
            $pharmacie=new Pharmacie();
            $form =$this->CreateForm(PharmacieType::class,$pharmacie);
          //$form->add('Ajouter',SubmitType::class);
            $form->handleRequest($request);
            if ($form->isSubmitted() && $form->isValid())
            {
                //image
                $uploadedFile = $form['image']->getData();
                $newFilename = md5(uniqid()) . '.' . $uploadedFile->guessExtension();
                $uploadDirectory = $this->getParameter('kernel.project_dir') . '/public/uploads';
                $uploadedFile->move($uploadDirectory, $newFilename);
                $pharmacie->setImage('uploads/' . $newFilename);   
                $em=$this->getDoctrine()->getManager();
                $em->persist($pharmacie);
                $em->flush();
                return $this->redirectToRoute('app_Affichep');
            }
            return $this->render('GestionPharmacie/Add.html.twig',['form'=>$form->createView()]);
        
        }
        #[Route('/editp/{id}', name: 'app_editp')]
        public function edit(PharmacieRepository $repository, $id, Request $request)
        {
            $pharmacie = $repository->find($id);
            $form = $this->createForm(PharmacieType::class, $pharmacie);
            //$form->add('Edit', SubmitType::class);
    
            $form->handleRequest($request);
            if ($form->isSubmitted() && $form->isValid()) {
                $em = $this->getDoctrine()->getManager();
                $em->flush(); // Correction : Utilisez la méthode flush() sur l'EntityManager pour enregistrer les modifications en base de données.
                return $this->redirectToRoute("app_Affichep");
            }
    
            return $this->render('GestionPharmacie/edit.html.twig', ['form' => $form->createView(), ]);
        }
        #[Route('/deletep/{id}', name: 'app_deletep')]
        public function delete($id, PharmacieRepository $repository)
        {
            $pharmacie = $repository->find($id);
    
            if (!$pharmacie) {
                throw $this->createNotFoundException('pharmacie non trouvé');
            }
    
            $em = $this->getDoctrine()->getManager();
            $em->remove($pharmacie);
            $em->flush();
    
            
            return $this->redirectToRoute('app_Affichep');
        }
        //ordonnance
}
