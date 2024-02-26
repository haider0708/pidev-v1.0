<?php

namespace App\Controller;

use App\Entity\Commande;
use App\Entity\Livreur;
use App\Form\CommandeType;
use App\Repository\CommandeRepository;
use Doctrine\Persistence\ManagerRegistry;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\RedirectResponse;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Twilio\Rest\Client;

class CommandeController extends AbstractController
{
    #[Route('/commande', name: 'app_commande')]
    public function index(): Response
    {
        return $this->render('commande/index.html.twig', [
            'controller_name' => 'CommandeController',
        ]);
    }
    #[Route('/addCommande/{id?0}', name: 'app_addCommande')]
    public function addCommande(ManagerRegistry $doctrine, Request $request,$id): Response
   
    {   
        $new=false;
        $repository = $doctrine->getRepository(Commande::class);
        $commande = $repository->find($id);
       
        if(!$commande)
        {    $new=true;
            $commande=new Commande();
        }
       

        //$ersonne est l image de formulaire
        $form=$this->createForm(CommandeType::class, $commande);

        $form->handleRequest($request);
        if($form->isSubmitted() && $form->isValid())
        {//recuperer les data
            $entitymanager=$doctrine->getManager();
            $entitymanager->persist($commande);
            $entitymanager->flush();
            if($new)
            {
                $message="a eté ajouté avec succes";
            }
            else
            {$message="a eté modifié avec succes";

            }
            $this->addFlash('success',  $message);
            
            return $this->redirectToRoute('app_afficheCommande');

        }
        else {
            return $this->render('commande/addCommande.html.twig',[
                    'form'=>$form->createView()

                ]

            );

        }


    }

    #[Route('/afficheCommande}', name: 'app_afficheCommande')]
    public function afficher(ManagerRegistry $doctrine): Response
    {
       
        $repository = $doctrine->getRepository(Commande::class);
        $commande = $repository->findAll();
        return $this->render('commande/afficherCommande.html.twig', [

            'commandes' => $commande
        ]);


    }
    #[Route('/deleteCommande/{id}', name: 'app_deleteCommande')]
    public function deleteCommande( ManagerRegistry $doctrine, $id): RedirectResponse
    {        $repository = $doctrine->getRepository(Commande::class);
           $commande = $repository->find($id);
            if ($commande) {
                $manager = $doctrine->getManager();
    
                $manager->remove($commande);
                $manager->flush();
                $this->addFlash('success', 'la Commande a ete supprimé avec succe');
               
    
            } else {
                $this->addFlash('error', 'la Commande inexistant');
    
            }
            return $this->redirectToRoute('app_afficheCommande');
        

    }
    #[Route('/detailCommande/{id<\d+>}', name: 'app_detailCommande')]
    public function detailCommande(ManagerRegistry $doctrine, $id): Response
    {
        $repository = $doctrine->getRepository(Commande::class);
        $commande = $repository->find($id);

        if (!$commande) {
            $this->addFlash('error', "la commande d'id $id n'existe pas");
            return $this->redirectToRoute('app_afficheCommande');
        }
        return $this->render('commande/detailCommande.html.twig', [

            'commande' => $commande
        ]);


    }
    #[Route('/addCommandeFront/{livreurId?}', name: 'app_addCommandeFront')]
    public function addCommandeFront(ManagerRegistry $doctrine, Request $request,$livreurId): Response
    {  $entityManager = $doctrine->getManager();
        $repository = $entityManager->getRepository(Livreur::class);
        $livreur = $repository->find($livreurId);
        $commande = new Commande();
        $commande->setLivreur($livreur);
        $form = $this->createForm(CommandeType::class, $commande);
       
        $form->remove('livreur');
    
       
        $form->handleRequest($request);
        if ($form->isSubmitted() && $form->isValid()) {
            $entityManager->persist($commande);
            $entityManager->flush();

            $accountSid='AC8f3d4e9f79cf0bd67b0a93b93b1f36d2';
            $authToken='f1d87ab9f1c33aaa2220f26c296855c9';
            $twilio= new Client($accountSid,$authToken);
            $message = $twilio->messages->create('+21653587130',array( 'from'=>'+18452432382','body'=>'Votre commande was detected!',));
            if ($message->sid) {
                $sms= 'SMS sent successfully.';
                $this->addFlash('success', " la commande a ete envoyée avec succeée");
                //$form->getData();
                return $this->redirectToRoute('app_afficheFront');
            } else {
                $sms ='Failed to send SMS.';
            }
        }
    
        return $this->render('commande/addCommandeFront.html.twig', [
            'form' => $form->createView(),
        ]);

      
        
       
    }
    #[Route('/triecc', name: 'triecroissantc')]
    public function trierparNomcroissant(CommandeRepository $repository): Response
    {
      $commandecroissant=$repository->triecroissant();
        return $this->render('commande/afficherCommande.html.twig', [
            "commandes"=> $commandecroissant
        ]);

    }
    #[Route('/triedc', name: 'triedecroissantc')]
    public function trierparnomdecroissant(CommandeRepository $repository): Response
    {
        $commandedecroissant=$repository->triedecroissant();
        return $this->render('commande/afficherCommande.html.twig', [
            "commandes"=>$commandedecroissant
        ]);

    }
}
