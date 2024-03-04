<?php

namespace App\Controller;

use Sun\Contract\Country;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
#[Route('/bot')]
class BotController extends AbstractController
{
    #[Route('/rendezvous/bot', name: 'app_bot')]
    public function index(Request $request): Response
    {
       $a = "fdg";
     
    
         
    $qa = [
        'Bonjour' => 'Bonjour ! Comment puis-je vous aider ?',
        'Comment se déroule la consultation en ligne ?'=>'La consultation se fait via vidéoconférence sécurisée',
        'Quels sont les médecins disponibles pour une consultation en ligne ?' => 'Actuellement, nos médecins disponibles pour des consultations en ligne sont le Dr Smith et le Dr Dupont.',
        'Comment puis-je payer pour la consultation en ligne ?' => 'Une fois votre rendez-vous confirmé, vous recevrez un lien de paiement',
        'Comment puis-je annuler ou reporter mon rendez-vous en ligne ?'=>'Vous pouvez annuler ou reporter votre rendez-vous en ligne en nous contactant directement par téléphone ou par e-mail',
        'fb'=>$a,
        'Mes informations médicales seront-elles sécurisées ?'=>'Absolument. Nous utilisons des technologies avancées pour protéger votre confidentialité.',
        'Quelles sont les dates disponibles ?' => 'fin de mois a partir de 12 ',
    ];
    $message = $request->request->get('message');
    if (array_key_exists($message, $qa)) {
        $response = $qa[$message];
    } else {
        $response = 'echec!';
    }
    return $this->render('bot/index.html.twig', [
        'response' => $response
    ]);

}}
