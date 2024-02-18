<?php

namespace App\Controller;

use App\Form\OrdonnanceType;
use App\Entity\Ordonnance;
use App\Entity\Pharmacie;
use App\Repository\OrdonnanceRepository;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\Form\Extension\Core\Type\SubmitType;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Doctrine\ORM\EntityManagerInterface;
class OrdonnanceController extends AbstractController
{
    #[Route('/ordonnance', name: 'app_ordonnance')]
    public function index(): Response
    {
        return $this->render('ordonnance/index.html.twig', [
            'controller_name' => 'OrdonnanceController',
        ]);
    }
    #[Route('/Afficheo', name: 'app_Afficheo')]


    public function Affiche (OrdonnanceRepository $repository)
        {
            $ordonnance=$repository->findAll() ; //select *
            return $this->render('ordonnance/Affiche.html.twig',['ordonnance'=>$ordonnance]);
        }
        #[Route('/Addo', name: 'app_Addo')]

        public function  Add (Request  $request)
        {
            $ordonnance=new Ordonnance();
            $form =$this->CreateForm(OrdonnanceType::class,$ordonnance);
           // $form->add('Ajouter',SubmitType::class);
            $form->handleRequest($request);
            if ($form->isSubmitted() && $form->isValid())
            {
                $em=$this->getDoctrine()->getManager();
                $em->persist($ordonnance);
                $em->flush();
                return $this->redirectToRoute('app_Afficheo');
            }
            return $this->render('ordonnance/Add.html.twig',['form'=>$form->createView()]);
        
        }
        #[Route('/edito/{id}', name: 'app_edito')]
        public function edit(OrdonnanceRepository $repository, $id, Request $request)
        {
            $ordonnance = $repository->find($id);
            $form = $this->createForm(OrdonnanceType::class, $ordonnance);
            //$form->add('Edit', SubmitType::class);
    
            $form->handleRequest($request);
            if ($form->isSubmitted() && $form->isValid()) {
                $em = $this->getDoctrine()->getManager();
                $em->flush(); // Correction : Utilisez la méthode flush() sur l'EntityManager pour enregistrer les modifications en base de données.
                return $this->redirectToRoute("app_Afficheo");
            }
    
            return $this->render('ordonnance/edit.html.twig', ['form' => $form->createView(), ]);
        }
        #[Route('/deleteo/{id}', name: 'app_deleteo')]
        public function delete($id, OrdonnanceRepository $repository)
        {
            $ordonnance = $repository->find($id);
    
            if (!$ordonnance) {
                throw $this->createNotFoundException('ordonnance non trouvé');
            }
    
            $em = $this->getDoctrine()->getManager();
            $em->remove($ordonnance);
            $em->flush();
    
            
            return $this->redirectToRoute('app_Afficheo');
        }
}
