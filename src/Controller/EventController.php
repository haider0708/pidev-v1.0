<?php
namespace App\Controller;



    use App\Entity\Event;
    use App\Form\Event1Type;
    use App\Repository\EventRepository;
    use Doctrine\ORM\EntityManagerInterface;
    use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
    use Symfony\Component\HttpFoundation\Request;
    use Symfony\Component\HttpFoundation\Response;
    use Symfony\Component\Routing\Annotation\Route;
    use Symfony\Component\Validator\Validator\ValidatorInterface;
    use Symfony\Component\Mailer\MailerInterface; // Ensure this use statement is correctly added at the top.
    use Symfony\Component\Mime\Email;
    use App\Form\EventTypeSelectorType;
    use Symfony\Component\HttpFoundation\RedirectResponse;
    use Dompdf\Dompdf;
    use Dompdf\Options;

    #[Route('/event')]
    class EventController extends AbstractController
    {
        #[Route('/', name: 'app_event_index', methods: ['GET', 'POST'])]
public function index(EventRepository $eventRepository, MailerInterface $mailer, Request $request): Response
{
    $form = $this->createForm(EventTypeSelectorType::class);
    $form->handleRequest($request);

    if ($form->isSubmitted() && $form->isValid()) {
        $data = $form->getData();
        $event = $eventRepository->find($data['event']);
        if ($event) {
            $email = (new Email())
                ->from('jarabnii@gmail.com')
                ->to($data['email'])
                ->subject("You're invited to " . $event->getTitre())
                ->html($this->renderView('event/invitation.html.twig', ['event' => $event]));

            $mailer->send($email);
            $this->addFlash('success', 'Invitation sent successfully!');
        }

        return new RedirectResponse($this->generateUrl('app_event_index'));
    }

    return $this->render('event/index.html.twig', [
        'events' => $eventRepository->findAll(),
        'form' => $form->createView(),
    ]);
}
        #[Route('/new', name: 'app_event_new', methods: ['GET', 'POST'])]
        public function new(Request $request, EntityManagerInterface $entityManager): Response
        {
            $event = new Event();
            $form = $this->createForm(Event1Type::class, $event);
            $form->handleRequest($request);

            if ($form->isSubmitted() && $form->isValid()) {
                $entityManager->persist($event);
                $entityManager->flush();

                return $this->redirectToRoute('app_event_index', [], Response::HTTP_SEE_OTHER);
            }
            else 

            return $this->renderForm('event/new.html.twig', [
                'event' => $event,
                'form' => $form,
            ]);
        }

        #[Route('/{id}', name: 'app_event_show', methods: ['GET'])]
        public function show(Event $event): Response
        {
            return $this->render('event/show.html.twig', [
                'event' => $event,
            ]);
        }

        #[Route('/{id}/edit', name: 'app_event_edit', methods: ['GET', 'POST'])]
        public function edit(Request $request, Event $event, EntityManagerInterface $entityManager, ValidatorInterface $validator): Response
        { 
            $form = $this->createForm(Event1Type::class, $event);
            $form->handleRequest($request);

            if ($form->isSubmitted() && $form->isValid()) {
                $entityManager->flush();

                return $this->redirectToRoute('app_event_index', [], Response::HTTP_SEE_OTHER);
            }

            return $this->renderForm('event/edit.html.twig', [
                'event' => $event,
                'form' => $form,
            ]);
        }

        #[Route('/{id}', name: 'app_event_delete', methods: ['POST'])]
        public function delete(Request $request, Event $event, EntityManagerInterface $entityManager): Response
        {
            if ($this->isCsrfTokenValid('delete'.$event->getId(), $request->request->get('_token'))) {
                $entityManager->remove($event);
                $entityManager->flush();
            }

            return $this->redirectToRoute('app_event_index', [], Response::HTTP_SEE_OTHER);
        }
        
        #[Route('/front/event', name: 'event_front')]
        public function frontAffiche(EventRepository $eventRepository): Response
        {
            // Récupération de tous les événements depuis la base de données
            $events = $eventRepository->findAll();
        
            // Passage des événements au template pour affichage
            return $this->render('front/indexFront.html.twig', [
                'events' => $eventRepository->findAll(),
            ]);
        
        
        }
        #[Route('/{id}/generatepdf', name: 'app_event_generate_pdf', methods: ['GET'])]
        public function generatePdf(Event $event): Response
        {
            $pdfOptions = new Options();
            $pdfOptions->set('isHtml5ParserEnabled', true); // Enable HTML5 parser for better CSS compatibility
            $dompdf = new Dompdf($pdfOptions);
            
            $html = $this->renderView('event/event_details_pdf.html.twig', ['event' => $event]);
            
            $dompdf->loadHtml($html);
            $dompdf->setPaper('A4', 'portrait');
            $dompdf->render();
            
            // Stream the file to the browser
            $dompdf->stream("event_" . $event->getId() . ".pdf", ["Attachment" => false]); // Set Attachment to true to download
        
            return new Response('', 200, ['Content-Type' => 'application/pdf']);
        }
        }
