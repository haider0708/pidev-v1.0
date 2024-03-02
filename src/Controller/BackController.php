<?php

namespace App\Controller;
use App\Form\DoctorType;
use App\Entity\Doctor;
use App\Repository\DoctorRepository;
use App\Form\PATIENTType;
use App\Entity\PATIENT;
use App\Repository\PATIENTRepository;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\Form\Extension\Core\Type\SubmitType;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\PasswordHasher\Hasher\UserPasswordHasherInterface;
use Doctrine\ORM\EntityManagerInterface;




class BackController extends AbstractController
{
    #[Route('/backcc', name: 'app_backcc')]
    public function index(): Response
    {
        return $this->render('back/index.html.twig', [
            'controller_name' => 'BackController',
        ]);
    }

    #[Route('/add_doctor', name: 'app_add_doctor')]

    public function  Add_Doctor (Request  $request)
    {
        $doctor=new Doctor();
        $form =$this->CreateForm(DoctorType::class,$doctor);
        $form->add('Ajouter',SubmitType::class);
        $form->handleRequest($request);
        if ($form->isSubmitted() && $form->isValid())
        {
            $uploadedFile = $form['img_path']->getData();
            $newFilename = md5(uniqid()) . '.' . $uploadedFile->guessExtension();
            $uploadDirectory = $this->getParameter('kernel.project_dir') . '/public/uploads';
            $uploadedFile->move($uploadDirectory, $newFilename);
            $doctor->setimg_path('uploads/' . $newFilename);
            $em=$this->getDoctrine()->getManager();
            $em->persist($doctor);
            $em->flush();
            return $this->redirectToRoute('app_affiche_doctor');
        }
        return $this->render('back/add_doctor.html.twig',['form'=>$form->createView()]);
    }    

    #[Route('/affiche_doctor', name: 'app_affiche_doctor')]


    public function Affiche_Doctor (DoctorRepository $repository)
        {
            $doctor=$repository->findAll() ; //select *
            return $this->render('back/affiche_doctor.html.twig',['doctor'=>$doctor]);
        }
        #[Route('/Edit_doctor/{id}', name: 'edit_doctor')]
    public function Edit_Doctor (DoctorRepository $repository, $id, Request $request)
    {
        $doctor = $repository->find($id);
        $form = $this->createForm(DoctorType::class, $doctor);
        $form->add('Edit', SubmitType::class);

        $form->handleRequest($request);
        if ($form->isSubmitted() && $form->isValid()) {
            $uploadedFile = $form['img_path']->getData();
            $newFilename = md5(uniqid()) . '.' . $uploadedFile->guessExtension();
            $uploadDirectory = $this->getParameter('kernel.project_dir') . '/public/uploads';
            $uploadedFile->move($uploadDirectory, $newFilename);
            $doctor->setimg_path('uploads/' . $newFilename);
            $em = $this->getDoctrine()->getManager();
            $em->flush(); // Correction : Utilisez la méthode flush() sur l'EntityManager pour enregistrer les modifications en base de données.
            return $this->redirectToRoute("app_affiche_doctor");
        }

        return $this->render('back/edit_doctor.html.twig', ['form' => $form->createView()]);
    }

    #[Route('/Delete_doctor/{id}', name: 'delete_doctor')]
    public function Delete_Doctor ($id, DoctorRepository $repository)
    {
        $doctor = $repository->find($id);

        if (!$doctor) {
            throw $this->createNotFoundException('doctor non trouvé');
        }

        $em = $this->getDoctrine()->getManager();
        $em->remove($doctor);
        $em->flush();

        
        return $this->redirectToRoute('app_affiche_doctor');
    }

    #[Route('/add_patient', name: 'app_add_patient')]
    public function addPatient(Request $request, UserPasswordHasherInterface $userPasswordHasher)
    {
        $patient=new Patient();
        $form =$this->CreateForm(PATIENTType::class,$patient);
        $form->add('Ajouter',SubmitType::class);
        $form->handleRequest($request);
        if ($form->isSubmitted() && $form->isValid())
        {
            // encode the plain password
                $patient->setPassword(
                $userPasswordHasher->hashPassword(
                $patient,
                $form->get('password')->getData()
                )
            );
            //
            $uploadedFile = $form['img_path']->getData();
            $newFilename = md5(uniqid()) . '.' . $uploadedFile->guessExtension();
            $uploadDirectory = $this->getParameter('kernel.project_dir') . '/public/uploads';
            $uploadedFile->move($uploadDirectory, $newFilename);
            $patient->setimg_path('uploads/' . $newFilename);
            $em=$this->getDoctrine()->getManager();
            $em->persist($patient);
            $em->flush();
            return $this->redirectToRoute('app_affiche_patient');
        }
        return $this->render('back/add_patient.html.twig',['form'=>$form->createView()]);
    }    

    #[Route('/affiche_patient', name: 'app_affiche_patient')]


    public function Affiche (PatientRepository $repository)
        {
            $patient=$repository->findAll() ; //select *
            return $this->render('back/affiche_patient.html.twig',['patient'=>$patient]);
        }
        
        #[Route('/edit_patient/{id}', name: 'edit_patient')]
    public function edit(PatientRepository $repository, $id, Request $request,UserPasswordHasherInterface $userPasswordHasher)
    {
        $patient = $repository->find($id);
        $form = $this->createForm(PATIENTType::class, $patient);
        $form->add('Edit', SubmitType::class);

        $form->handleRequest($request);
        if ($form->isSubmitted() && $form->isValid()) {
            // encode the plain password
            $patient->setPassword(
                $userPasswordHasher->hashPassword(
                $patient,
                $form->get('password')->getData()
                )
            );
            
            $uploadedFile = $form['img_path']->getData();
            $newFilename = md5(uniqid()) . '.' . $uploadedFile->guessExtension();
            $uploadDirectory = $this->getParameter('kernel.project_dir') . '/public/uploads';
            $uploadedFile->move($uploadDirectory, $newFilename);
            $patient->setimg_path('uploads/' . $newFilename);
            $em = $this->getDoctrine()->getManager();
            $em->flush(); // Correction : Utilisez la méthode flush() sur l'EntityManager pour enregistrer les modifications en base de données.
            return $this->redirectToRoute("app_affiche_patient");
        }

        return $this->render('back/edit_patient.html.twig', [
            'form' => $form->createView(),
        ]);
    }
    #[Route('/delete_patient/{id}', name: 'delete_patient')]
    public function delete($id, PatientRepository $repository)
    {
        $patient = $repository->find($id);

        if (!$patient) {
            throw $this->createNotFoundException('patient non trouvé');
        }

        $em = $this->getDoctrine()->getManager();
        $em->remove($patient);
        $em->flush();

        
        return $this->redirectToRoute('app_affiche_patient');
    }
}
