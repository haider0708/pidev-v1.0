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
use App\Repository\LivreurRepository;
use Symfony\UX\Chartjs\Builder\ChartBuilderInterface;
use Symfony\UX\Chartjs\Model\Chart;


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

            $accountSid='AC82a6c5594c34219cb358483dc1e4782d';
            $authToken='122bb070a4b2a44ed6255a43e7803e48';
            $twilio= new Client($accountSid,$authToken);
            $message = $twilio->messages->create('+21622550734',array( 'from'=>'+16206225746','body'=>'Votre commande was detected!',));
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

    #[Route('/stat', name: 'app_stat')]
    public function showStat(LivreurRepository $livreurRepository, CommandeRepository $commandeRepository, ChartBuilderInterface $chartBuilder): Response
    {
        $livreurs = $livreurRepository->findAll();

    $labels = [];
    $data = [];

    foreach ($livreurs as $livreur) {
        $labels[] = $livreur->getNom(); // Assuming Nom is the property you want as labels

        // Count the number of commandes for each livreur
        $commandesCount = $commandeRepository->countByLivreur($livreur); // Assuming you have a custom method in your repository to count commandes by livreur
        $data[] = $commandesCount;
    }

    // Create the chart data
    $chartData = [];
    foreach ($labels as $index => $label) {
        $chartData[] = ['label' => $label, 'value' => $data[$index]];
    }

    return $this->render('commande/stats.html.twig', [
        'chartData' => $chartData, // Pass the chart data to Twig
    ]);
}
}
