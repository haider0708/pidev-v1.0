<?php

namespace App\Controller;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use App\Entity\Rendezvous;
use App\Entity\Rapport;
use App\Form\RapportType;
use App\Form\RendezvoussType;
use App\Repository\RendezvousRepository;
use App\Repository\RapportRepository;
use Doctrine\Persistence\ManagerRegistry;
use SebastianBergmann\CodeCoverage\Report\Html\Renderer;
use Symfony\Component\HttpFoundation\Request;

class BackController extends AbstractController
{
    #[Route('/back', name: 'app_back')]
    public function index(): Response
    {
        return $this->render('back/indexdashbord.html.twig', [
            'controller_name' => 'BackController',
        ]);
    }
    #[Route('/backdash', name: 'app_backdash')]
    public function indexdash(): Response
    {
        return $this->render('back/indexdashbord.html.twig', [
            'controller_name' => 'BackController',
        ]);
    }
    #[Route('/show', name: 'show')] 
    public function show(RendezvousRepository $repo): Response
    {
        $result = $repo->findAll();

        return $this->render('rendezvous/test.html.twig', [
            'response' => $result
        ]);
    }

    #[Route('/addrr', name: 'addrr')] 
    public function addF(ManagerRegistry $mr,RapportRepository $repo,Request $req): Response
    {
       
        $p = new Rendezvous();//-1instance
        $form=$this->createform(RendezvoussType::class,$p);///2-
        $form->handleRequest($req);
        if($form->isSubmitted()){
            $em = $mr->getManager(); ///3- persist flush
            $em->persist($p);
            $em->flush();
            return $this ->redirectToRoute('show');    
        }
        
        return $this-> render('back/add.html.twig',[
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
    
        return $this->render('rendezvous/update.html.twig', [
            'f' => $form->createView()
        ]);
    }
    #[Route('/addfact', name: 'addfact')] 
    public function addfact(ManagerRegistry $mr,RapportRepository $repo, Request $req): Response
    {
       
        $p = new Rapport();//-1instance
        $form=$this->createform(RapportType::class,$p);///2-
        $form->handleRequest($req);
        if($form->isSubmitted()){
            $em = $mr->getManager(); ///3- persist flush
            $em->persist($p);
            $em->flush();
            return $this ->redirectToRoute('showF');    
        }
        
        return $this-> render('rapport/addfact.html.twig',[
            'f'=>$form->createView()
        ]);
    }
}
