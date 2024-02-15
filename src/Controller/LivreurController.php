<?php

namespace App\Controller;

use App\Entity\Livreur;
use App\Form\LivreurType;
use App\Repository\CommandeRepository;
use Doctrine\Persistence\ManagerRegistry;
use Psr\Log\LoggerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\File\Exception\FileException;
use Symfony\Component\HttpFoundation\JsonResponse;
use Symfony\Component\HttpFoundation\RedirectResponse;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

use Symfony\Component\String\Slugger\SluggerInterface;

class LivreurController extends AbstractController
{
    #[Route('/livreur', name: 'app_livreur')]
    public function index(): Response
    {
        return $this->render('livreur/index.html.twig', [
            'controller_name' => 'LivreurController',
        ]);
    }
    #[Route('/back', name: 'app_back')]
    public function showBack(): Response
    {
        return $this->render('backTemplate.html.twig');
    }
    #[Route('/afficheLivreurs', name: 'app_affiche')]
    public function afficher(ManagerRegistry $doctrine): Response
    {
       
        $repository = $doctrine->getRepository(Livreur::class);
        $livreur = $repository->findAll();
        return $this->render('livreur/afficherLivreur.html.twig', [

            'livreurs' => $livreur
        ]);


    }
    #[Route('/addLivereur/{id?0}', name: 'app_addLivereur')]
    public function addLivreur(ManagerRegistry $doctrine, Request $request,$id,SluggerInterface $slugger): Response
   
    {   
        $new=false;
        $repository = $doctrine->getRepository(Livreur::class);
        $livreur = $repository->find($id);
       
        if(!$livreur)
        {    $new=true;
            $livreur=new Livreur();
        }


        //$ersonne est l image de formulaire
        $form=$this->createForm(LivreurType::class,$livreur);

        $form->handleRequest($request);
        if($form->isSubmitted() && $form->isValid())
        {//recuperer les data
            $brochureFile = $form->get('image')->getData();

            // this condition is needed because the 'brochure' field is not required
            // so the PDF file must be processed only when a file is uploaded
            if ($brochureFile) {
                $originalFilename = pathinfo($brochureFile->getClientOriginalName(), PATHINFO_FILENAME);
                // this is needed to safely include the file name as part of the URL
                $safeFilename = $slugger->slug($originalFilename);
                $newFilename = $safeFilename.'-'.uniqid().'.'.$brochureFile->guessExtension();

                // Move the file to the directory where brochures are stored
                try {
                    $brochureFile->move(
                        $this->getParameter('livreur_directory'),
                        $newFilename
                    );
                } catch (FileException $e) {
                    // ... handle exception if something happens during file upload
                }

                // updates the 'brochureFilename' property to store the PDF file name
                // instead of its contents
                $livreur->setImage($newFilename);
            }
            $entitymanager=$doctrine->getManager();
            $entitymanager->persist($livreur);
            $entitymanager->flush();
            if($new)
            {
                $message="a eté ajouté avec succes";
            }
            else
            {$message="a eté modifié avec succes";

            }
            $this->addFlash('success',  $message);
            
            return $this->redirectToRoute('app_affiche');

        }
        else {
            return $this->render('livreur/addLivreur.html.twig',[
                    'form'=>$form->createView()

                ]

            );

        }


    }
    #[Route('/deleteLivreur/{id}', name: 'app_deleteLivreur')]
    public function deleteLivreur( ManagerRegistry $doctrine, $id): RedirectResponse
    {        $repository = $doctrine->getRepository(Livreur::class);
           $livreur = $repository->find($id);
            if ($livreur) {
                $manager = $doctrine->getManager();
    
                $manager->remove($livreur);
                $manager->flush();
                $this->addFlash('success', 'le Livreur a ete supprimé avec succe');
               
    
            } else {
                $this->addFlash('error', 'le Livreur inexistant');
    
            }
            return $this->redirectToRoute('app_affiche');
        

    }
    #[Route('/detailLivreur/{id<\d+>}', name: 'app_detailLivreur')]
    public function detailLivreur(ManagerRegistry $doctrine, $id): Response
    {
        $repository = $doctrine->getRepository(Livreur::class);
        $livreur = $repository->find($id);

        if (!$livreur) {
            $this->addFlash('error', "le livreur d'id $id n'existe pas");
            return $this->redirectToRoute('app_affiche');
        }
        return $this->render('livreur/detailLivreur.html.twig', [

            'livreur' => $livreur
        ]);


    }
    #[Route('/livreur/{id}/commands', name: 'livreur_commands')]
    public function livreurCommands($id,CommandeRepository $commandeRepository) :Response
    {
        $commands = $commandeRepository->findAllByLivreur($id);
        return $this->render('commande/commandes.html.twig', [
            'commands' => $commands,
        ]);
       


    }
    #[Route('/afficheLivreursFront', name: 'app_afficheFront')]
    public function afficherFront(ManagerRegistry $doctrine): Response
    {
       
        $repository = $doctrine->getRepository(Livreur::class);
        $livreur = $repository->findAll();
        return $this->render('livreur/afficherLivreurFront.html.twig', [

            'livreurs' => $livreur
        ]);


    }
    #[Route('/front', name: 'app_Front')]
    public function showFront(): Response
    {
        return $this->render('frontTemplate.html.twig');
    }
}
