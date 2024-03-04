<?php

namespace App\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use App\Entity\Rendezvous;
use App\Entity\Rapport;
use App\Form\RendezvoussType;
use App\Repository\RendezvousRepository;
use App\Repository\RapportRepository;
use Doctrine\Persistence\ManagerRegistry;
use SebastianBergmann\CodeCoverage\Report\Html\Renderer;
use Symfony\Component\HttpFoundation\Request;
use Joli\JoliNotif\Notification;
use Joli\JoliNotif\NotifierFactory;
use Dompdf\Dompdf;
use Dompdf\Options;
use Symfony\Component\HttpFoundation\ResponseHeaderBag;
use Symfony\UX\Chartjs\Builder\ChartBuilderInterface;
use Symfony\UX\Chartjs\Model\Chart;
use App\Service\SmsGenerator;





class RendezvousController extends AbstractController
{


    #[Route('/rendezvous', name: 'app_rendezvous')]
    public function index(): Response
    {
        return $this->render('rendezvous/index.html.twig', [
            'controller_name' => 'RendezvousController',
        ]);
    }


    #[Route('/statistics', name: 'app_statistics')]
    public function statistics(RendezvousRepository $rendezvousRepository): Response
    {
        // Collecter les données nécessaires pour les statistiques
        $rendezvouss = $rendezvousRepository->findAll();
        $totalRendezvous = count($rendezvouss);
        // Effectuer les calculs nécessaires
        // Par exemple, calculer le nombre total de rendez-vous

        // Passer les statistiques à la vue
        return $this->render('rendezvous/statistics.html.twig', [
            'totalRendezvous' => $totalRendezvous,
            // Autres statistiques que vous souhaitez afficher
        ]);
    }

    #[Route('/chart', name: 'rendezvous_chart')]
    public function chart(ChartBuilderInterface $chartBuilder): Response
    {
        // Créer un graphique Chart.js
        $chart = $chartBuilder->createChart(Chart::TYPE_BAR);
        $chart->setData([
            'labels' => ['Red', 'Blue', 'Yellow', 'Green', 'Purple', 'Orange'],
            'datasets' => [
                [
                    'label' => 'Example Dataset',
                    'backgroundColor' => 'rgb(255, 99, 132)',
                    'borderColor' => 'rgb(255, 99, 132)',
                    'data' => [12, 19, 3, 5, 2, 3],
                ],
            ],
        ]);

        return $this->render('rendezvous/chart.html.twig', [
            'chart' => $chart,
        ]);
    }

    #[Route('/search', name: 'app_rendezvous_test', methods: ['POST'])] // Utiliser la méthode POST pour la recherche
    public function AfficherAction(RendezvousRepository $rendezvousRepository, Request $request)
    {
        $search = $request->request->get('searchString'); // Utiliser request->request pour récupérer les données POST
        $rendezvouss = $rendezvousRepository->findMulti($search);
        return $this->render("rendezvous/test.html.twig", array(
            'rendezvouss' => $rendezvouss
        ));
    }


   
    #[Route('/addF', name: 'addF')] 
    public function addF(ManagerRegistry $mr,RapportRepository $repo,Request $req): Response
    {
       
        $p = new Rendezvous();//-1instance
        $form=$this->createform(RendezvoussType::class,$p);///2-
        $form->handleRequest($req);
        if($form->isSubmitted()){
            $em = $mr->getManager(); ///3- persist flush
            $em->persist($p);
            $em->flush();
           
            // Create a Notifier
         $notifier = NotifierFactory::create();
        
// Create your notification
$notification =
    (new Notification())
    ->setTitle('Health Arena Notification')
    ->setBody('votre reservation d un rdv est bien enregistré ')
   
;

// Send it
$notifier->send($notification);
            return $this ->redirectToRoute('show');    
        }
        
        return $this-> render('rendezvous/add.html.twig',[
            'f'=>$form->createView()
        ]);
    }


    #[Route('/remove/{id}', name: 'remove')]
    public function remove(RendezvousRepository $repo, $id, ManagerRegistry $mr): Response
    {
        $rendezvous = $repo->find($id);
        $em = $mr->getManager();
        $em->remove($rendezvous);
        $em->flush();
    
        return $this->redirectToRoute('show');
    }





    #[Route('/update/{id}', name: 'update')]
    public function updateRendezvous(int $id, ManagerRegistry $mr, Request $req, RendezvousRepository $repo): Response
    {
        $p = $repo->find($id); 
    
        if (!$p) {
            throw $this->createNotFoundException('Rendez vous n existe pas.');
        }
    
        $form = $this->createForm(RendezvoussType::class, $p); 
    
        $form->handleRequest($req);
    
        if ($form->isSubmitted() && $form->isValid()) {
            $em = $mr->getManager();
            $em->flush();
    
            return $this->redirectToRoute('show');
        }
    
        return $this->render('rendezvous/add.html.twig', [
            'f' => $form->createView()
        ]);
    }
    




    #[Route('/generate/pdf', name: 'Extract_data', methods: ['GET' , 'POST'])]
    public function generatePdf(RendezvousRepository $cr): Response
{ 
    $rendezvouss = $cr->findAll();
    $currentDate = new \DateTime();
    $options = new Options();
    $options->set('isHtml5ParserEnabled', true);
    $options->set('isPhpEnabled', true);
    $dompdf = new Dompdf($options);
    $html = $this->renderView('rendezvous/generatePdf.html.twig', ['rendezvouss' => $rendezvouss , 'date' => $currentDate]);
    $dompdf->loadHtml($html);
    $dompdf->setPaper('A4', 'portrait');
    $dompdf->render();  
    $pdfContent = $dompdf->output();
    $response = new Response();
    $response->setContent($pdfContent);
    $response->headers->set('Content-Type', 'application/pdf');

    $disposition = $response->headers->makeDisposition(
        ResponseHeaderBag::DISPOSITION_ATTACHMENT,
        'generatePdf.pdf'
    );
    $response->headers->set('Content-Disposition', $disposition);
    return $response;  
}
}