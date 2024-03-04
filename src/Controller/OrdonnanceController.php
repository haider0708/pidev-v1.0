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
use Dompdf\Dompdf;
use Dompdf\Options;
use Symfony\Component\OptionsResolver\OptionsResolver;
use Symfony\Component\Mailer\Mailer;
use Symfony\Component\Mailer\Transport;
use Symfony\Component\Mime\Email;

class OrdonnanceController extends AbstractController
{
    #[Route('/ordonnance', name: 'app_ordonnance')]
    public function index(OrdonnanceRepository $ordonnanceRepository): Response
    {   
        $ordonnances = $OrdonnanceRepository->findAll();

        return $this->render('ordonnance/index.html.twig', [
            'controller_name' => 'OrdonnanceController',
            'pharmacies'=> $pharmacies,
            'ordonnance' => $ordonnances,

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
                //mailing
                $transport = Transport::fromDsn('smtp://drissmanel6@gmail.com:yglepnhxvfjbjcka@smtp.gmail.com:587');
            $mailer = new Mailer($transport);
            $email = (new Email())
                ->from('drissmanel6@gmail.com')
                ->to('haydar.boudhrioua@gmail.com')
                ->subject('l ordonnance est ajouté avec succes')
                ->html('salut test');
                
          $mailer->send($email);

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


        //#[Route('/api/search/ordonnance', name: 'api_search_ordonnance', methods: ['GET'])]
    // public function advancedSearch(Request $request, OrdonnanceRepository $ordonnanceRepository): Response
    // {
            // Retrieve advanced search parameters from the request
        //   $medecinName = $request->query->get('medecin_name');
         //  $patientName = $request->query->get('patient_name');
          //  $startDate = $request->query->get('start_date');
         // $endDate = $request->query->get('end_date');
    
            // Use these parameters to perform an advanced search
           // $results = $ordonnanceRepository->advancedSearch($medecinName, $patientName, $startDate, $endDate);
    
            // You can customize the response format based on your needs
        //   return $this->json(['results' => $results]);
        //}
        #[Route('/searchMedecin', name: 'searchMedecin')]
        public function searchPost(Request $request, OrdonnanceRepository $OrdonnanceRepository): Response
        {
            $keyword = $request->query->get('keyword');
        
            // Requête pour rechercher les posts contenant exactement le mot-clé dans le titre
            $ordonnance = $OrdonnanceRepository->createQueryBuilder('m')
                ->andWhere('m.nommedecin LIKE :keyword')
                ->setParameter('keyword', '%'.$keyword.'%')
                ->orderBy('m.datedecreation', 'DESC')
                ->getQuery()
                ->getResult();
                
        
            return $this->render('front/affichefrontord.html.twig', [
                'ordonnance' => $ordonnance,
            ]);
        }
        #[Route('/listordonnance', name: 'list_ordonnance')]
        public function index1(OrdonnanceRepository $repo): Response
        {
            $pdfOptions = new Options();
    
            $pdfOptions-> set('defaultFont','Arial');
    
            $dompdf= new Dompdf($pdfOptions);
            $ordonnance=$repo->findAll();
           
            $html= $this->renderView('ordonnance/listordonnance.html.twig', [
                'ordonnance'=>$ordonnance,
                
        ]);
    
        $dompdf->loadHtml($html);
        $dompdf->setPaper('A4');
        $dompdf->render();
        return new Response($dompdf->output(), 200, [
            'Content-Type' => 'application/pdf',
            'Content-Disposition' => 'inline; filename="sample.pdf"',
        ]);
      
        }

    
}
